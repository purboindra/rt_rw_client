package org.purboyndradev.rt_rw.core.network

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.HttpResponseValidator
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BearerTokens
import io.ktor.client.plugins.auth.providers.bearer
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.serialization.json.Json
import org.purboyndradev.rt_rw.PlatformConfig
import org.purboyndradev.rt_rw.core.data.datastore.AppAuthRepository
import org.purboyndradev.rt_rw.core.data.datastore.AuthTokenStore
import org.purboyndradev.rt_rw.core.data.dto.RefreshTokenDto
import org.purboyndradev.rt_rw.core.data.dto.ResponseDto
import org.purboyndradev.rt_rw.core.data.remote.params.RefreshTokenParams
import co.touchlab.kermit.Logger as KermitLogger
import io.ktor.client.plugins.logging.Logger as KtorLogger

object HttpClientFactory {
    suspend fun create(
        engine: HttpClientEngine,
        appAuthRepository: AppAuthRepository,
        tokenRefresher: TokenRefresher,
        tokenStore: AuthTokenStore,
    ): HttpClient {

        if (tokenStore.memory.value == null) {
            val accessToken = appAuthRepository.accessTokenFlow.firstOrNull()
            val refreshToken = appAuthRepository.refreshTokenFlow.firstOrNull()
            if (!accessToken.isNullOrBlank() && !refreshToken.isNullOrBlank()) {
                KermitLogger.i("HttpClientFactory") { "Pre-populated tokenStore from DataStore." }
                tokenStore.memory.value = BearerTokens(accessToken, refreshToken)
            } else {
                KermitLogger.i("HttpClientFactory") { "No initial tokens found in DataStore." }
            }
        }

        return HttpClient(engine) {
            install(ContentNegotiation) {
                json(json = Json {
                    ignoreUnknownKeys = true
                    prettyPrint = true
                    isLenient = true
                })
            }

            KermitLogger.w("HttpClientFactory") {
                "Auth Plugin Installation. Current BearerTokens: ${
                    tokenStore.memory.value?.accessToken?.take(
                        10
                    )
                }..."
            }

            expectSuccess = true
            install(HttpTimeout) {
                socketTimeoutMillis = 20_000L
                requestTimeoutMillis = 20_000L
            }
            install(Logging) {
                level = LogLevel.ALL
                logger = object : KtorLogger {
                    override fun log(message: String) {
                        KermitLogger.i("HttpClient") { message }
                    }
                }
            }

            HttpResponseValidator {
                handleResponseExceptionWithRequest { exception, request ->
                    val clientException = exception as? ClientRequestException
                        ?: return@handleResponseExceptionWithRequest
                    val exceptionResponse = clientException.response

                    if (exceptionResponse.status == HttpStatusCode.NotFound) {
                        val exceptionResponseText = exceptionResponse.bodyAsText()
                        throw Exception(exceptionResponseText)
                    }
                }
            }

            install(Auth) {

                KermitLogger.w("HttpClientFactory") {
                    "Auth"
                    "BearerTokens: ${tokenStore.memory.value}"
                }

                bearer {
                    loadTokens {
                        tokenStore.memory.value
                    }

                    refreshTokens {
                        KermitLogger.d("Ktor Auth") { "refreshTokens triggered: ${oldTokens?.refreshToken}" }
                        val newTokens = tokenRefresher.refresh(oldTokens)
                        if (newTokens != null) {
                            tokenStore.memory.value = newTokens
                        }
                        newTokens
                    }

//                    sendWithoutRequest { req ->
//                        val p = req.url.encodedPath
//
//                        val shouldExclude = p.endsWith("/auth/sign-in") ||
//                                p.endsWith("/auth/otp/verify") ||
//                                p.endsWith("/auth/refresh-token")
//
//                        KermitLogger.i("Ktor Auth") { "Path: '$p'. Excluding from Auth header: $shouldExclude" }
//
//                        shouldExclude
//                    }
                }
            }
            defaultRequest {
                url(PlatformConfig.baseUrl)
                contentType(ContentType.Application.Json)
            }
        }
    }
}


/// FUN FOR HANDLE REFRESH TOKEN
class TokenRefresher(
    private val authHttpClient: HttpClient,
    private val appAuthRepository: AppAuthRepository,
) {
    private val mutex = Mutex()

    suspend fun refresh(oldTokens: BearerTokens?): BearerTokens? = mutex.withLock {
        KermitLogger.w("TokenRefresher") { "Attempting to refresh token." }

        val oldRefreshToken = oldTokens?.refreshToken ?: run {
            KermitLogger.e("TokenRefresher") { "Cannot refresh: No old refresh token available." }
            return@withLock null
        }

        KermitLogger.w("TokenRefresher") {
            "Execute refresh token: $oldRefreshToken"
        }

        try {
            val response: ResponseDto<RefreshTokenDto> =
                authHttpClient.post("api/v1/auth/refresh-token") {
                    setBody(RefreshTokenParams(oldRefreshToken))
                }.body()

            val newAccess = response.data?.accessToken
            val newRefresh = response.data?.refreshToken

            if (newAccess.isNullOrBlank() || newRefresh.isNullOrBlank()) {
                KermitLogger.e("TokenRefresher") { "Refresh call succeeded but response contained no tokens." }
                appAuthRepository.clearTokens()
                return@withLock null
            }

            KermitLogger.i("TokenRefresher") { "Successfully refreshed tokens." }

            appAuthRepository.saveTokens(newAccess, newRefresh)

            return@withLock BearerTokens(newAccess, newRefresh)

        } catch (e: Exception) {
            KermitLogger.e("TokenRefresher") { "Exception during token refresh: ${e.message}" }
            appAuthRepository.clearTokens()
            return@withLock null
        }
    }
}