package org.purboyndradev.rt_rw.core.data.remote.impl

import io.ktor.client.HttpClient
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import kotlinx.serialization.Serializable
import org.purboyndradev.rt_rw.core.data.dto.RefreshTokenDto
import org.purboyndradev.rt_rw.core.data.dto.SignInDto
import org.purboyndradev.rt_rw.core.data.dto.ResponseDto
import org.purboyndradev.rt_rw.core.data.dto.VerifyOtpDto
import org.purboyndradev.rt_rw.core.data.remote.api.AuthApi
import org.purboyndradev.rt_rw.core.data.remote.params.RefreshTokenParams
import org.purboyndradev.rt_rw.core.data.remote.params.SignInParams
import org.purboyndradev.rt_rw.core.data.remote.params.VerifyOtpParams
import org.purboyndradev.rt_rw.core.domain.DataError
import org.purboyndradev.rt_rw.core.domain.Result
import org.purboyndradev.rt_rw.core.network.safeCall
import org.purboyndradev.rt_rw.helper.BASE_URL


class KtorAuthRemoteDatasource(private val httpClient: HttpClient) :
    AuthApi {
    override suspend fun signIn(phoneNumber: String): Result<ResponseDto<SignInDto>, DataError.Remote> {
        return safeCall {
            httpClient.post("$BASE_URL/auth/sign-in") {
                setBody(SignInParams(phoneNumber))
            }
        }
    }
    
    override suspend fun verifyOtp(
        phoneNumber: String,
        otp: String
    ): Result<ResponseDto<VerifyOtpDto>, DataError.Remote> {
        return safeCall {
            httpClient.post("${BASE_URL}/auth/otp/verify") {
                setBody(VerifyOtpParams(phone = phoneNumber, otp))
            }
        }
    }
    
    override suspend fun refreshToken(
        accessToken: String,
        refreshToken: String
    ): Result<ResponseDto<RefreshTokenDto>, DataError.Remote> {
        return safeCall {
            httpClient.post("$BASE_URL/auth/refresh-token") {
                setBody(
                    RefreshTokenParams(
                        accessToken, refreshToken
                    )
                )
            }
        }
    }
}