package org.purboyndradev.rt_rw.core.network

import io.ktor.client.HttpClient
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BearerTokens
import io.ktor.client.plugins.auth.providers.bearer
import io.ktor.client.plugins.cache.HttpCache
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.DEFAULT
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.http.encodedPath
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.serialization.json.Json
import org.purboyndradev.rt_rw.PlatformConfig
import org.purboyndradev.rt_rw.core.data.datastore.AppAuthRepository
import org.purboyndradev.rt_rw.core.data.datastore.AuthTokenStore
import org.purboyndradev.rt_rw.core.domain.Result
import org.purboyndradev.rt_rw.domain.usecases.RefreshTokenUseCase

object HttpClientFactory {
    fun create(
        engine: HttpClientEngine,
        appAuthRepository: AppAuthRepository,
        tokenRefresher: TokenRefresher,
        tokenStore: AuthTokenStore,
        
        ): HttpClient {
        val client =  HttpClient(engine) {
            install(ContentNegotiation) {
                json(json = Json {
                    ignoreUnknownKeys = true
                    prettyPrint = true
                    isLenient = true
                })
            }
            install(HttpTimeout) {
                socketTimeoutMillis = 20_000L
                requestTimeoutMillis = 20_000L
            }
            install(Logging) {
                logger = Logger.DEFAULT
                level = LogLevel.ALL
            }
            install(HttpCache)
            install(Auth) {
                bearer {
                    loadTokens {
                        tokenStore.memory.value
                            ?: run {
                                val accessToken =
                                    appAuthRepository.accessTokenFlow.firstOrNull()
                                val refreshToken =
                                    appAuthRepository.refreshTokenFlow.firstOrNull()
                                if (!accessToken.isNullOrBlank() && !refreshToken.isNullOrBlank()) {
                                    val t = BearerTokens(
                                        accessToken = accessToken,
                                        refreshToken
                                    )
                                    tokenStore.memory.value = t
                                    t
                                } else null
                            }
                    }
                    refreshTokens {
                        val responseRefresh = tokenRefresher.refresh(oldTokens)
                        if (responseRefresh != null) tokenStore.memory.value =
                            responseRefresh
                        responseRefresh
                    }
                    sendWithoutRequest { req ->
                        val p = req.url.encodedPath
                        p.contains("/auth/sign-in") ||
                                p.contains("/auth/otp/verify") ||
                                p.contains("/auth/refresh-token")
                    }
                }
            }
            defaultRequest {
                url(PlatformConfig.baseUrl)
                contentType(ContentType.Application.Json)
            }
        }
        return client
    }
}


/// FUN FOR HANDLE REFRESH TOKEN
class TokenRefresher(
    private val refreshTokenUseCase: RefreshTokenUseCase,
    private val appAuthRepository: AppAuthRepository
) {
    private val mutex = Mutex()
    
    suspend fun refresh(oldTokens: BearerTokens?): BearerTokens? =
        mutex.withLock {
            val oldRefresh = oldTokens?.refreshToken ?: return null
            
            val curAccess = appAuthRepository.accessTokenFlow.firstOrNull()
            val curRefresh = appAuthRepository.refreshTokenFlow.firstOrNull()
            if (!curAccess.isNullOrBlank() && !curRefresh.isNullOrBlank() && curRefresh != oldRefresh) {
                return BearerTokens(curAccess, curRefresh)
            }
            
            return when (val res = refreshTokenUseCase.invoke(oldRefresh)) {
                is Result.Success -> {
                    val newTokens = res.data
                    appAuthRepository.saveTokens(
                        newTokens.accessToken,
                        newTokens.refreshToken
                    )
                    BearerTokens(newTokens.accessToken, newTokens.refreshToken)
                }
                
                is Result.Error -> null
            }
        }
}
