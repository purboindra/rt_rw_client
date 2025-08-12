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
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.serialization.json.Json
import org.purboyndradev.rt_rw.core.data.datastore.AppAuthRepository
import org.purboyndradev.rt_rw.core.domain.Result
import org.purboyndradev.rt_rw.domain.usecases.RefreshTokenUseCase

object HttpClientFactory {
    fun create(
        engine: HttpClientEngine,
        appAuthRepository: AppAuthRepository,
        refreshTokenUseCase: RefreshTokenUseCase
    ): HttpClient {
        return HttpClient(engine) {
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
                        val accessToken =
                            appAuthRepository.accessTokenFlow.firstOrNull()
                        val refreshToken =
                            appAuthRepository.refreshTokenFlow.firstOrNull()
                        
                        if (accessToken != null) {
                            BearerTokens(accessToken, refreshToken ?: "")
                        } else {
                            null
                        }
                    }
                    
                    refreshTokens {
                        val oldRefreshToken =
                            this.oldTokens?.refreshToken
                        if (oldRefreshToken != null) {
                            println("Ktor Auth: Attempting to refresh token...")
                            try {
                                val responseRefresh =
                                    refreshTokenUseCase.invoke(oldRefreshToken)
                                
                                when (responseRefresh) {
                                    is Result.Success -> {
                                        val newTokens = responseRefresh.data
                                        appAuthRepository.saveTokens(
                                            newTokens.accessToken,
                                            newTokens.refreshToken
                                        )
                                        BearerTokens(
                                            newTokens.accessToken,
                                            newTokens.refreshToken
                                        )
                                    }
                                    
                                    is Result.Error -> {
                                        null
                                    }
                                }
                                
                                val currentAccessToken =
                                    appAuthRepository.accessTokenFlow.firstOrNull()
                                val currentRefreshToken =
                                    appAuthRepository.refreshTokenFlow.firstOrNull()
                                if (currentAccessToken != null && currentRefreshToken != null) {
                                    BearerTokens(
                                        currentAccessToken,
                                        currentRefreshToken
                                    )
                                } else {
                                    null
                                }
                            } catch (e: Exception) {
                                println("Ktor Auth: Refresh token failed: $e")
                                null
                            }
                        } else {
                            null
                        }
                    }
                    sendWithoutRequest { request ->
                        request.url.encodedPathSegments.contains("auth/sign-in") ||
                                request.url.encodedPathSegments.contains("auth/otp/verify")
                    }
                }
            }
            defaultRequest {
                contentType(ContentType.Application.Json)
            }
        }
    }
}