package org.purboyndradev.rt_rw.core.data.remote.impl

import io.ktor.client.HttpClient
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import org.purboyndradev.rt_rw.core.data.dto.RefreshTokenDto
import org.purboyndradev.rt_rw.core.data.dto.ResponseDto
import org.purboyndradev.rt_rw.core.data.dto.SignInDto
import org.purboyndradev.rt_rw.core.data.dto.VerifyOtpDto
import org.purboyndradev.rt_rw.core.data.remote.api.AuthApi
import org.purboyndradev.rt_rw.core.data.remote.params.RefreshTokenParams
import org.purboyndradev.rt_rw.core.data.remote.params.SignInParams
import org.purboyndradev.rt_rw.core.data.remote.params.VerifyOtpParams
import org.purboyndradev.rt_rw.core.domain.AppError
import org.purboyndradev.rt_rw.core.domain.Result
import org.purboyndradev.rt_rw.core.network.safeCallWrapped


class KtorAuthRemoteDatasource(private val httpClient: HttpClient) :
    AuthApi {
    override suspend fun signIn(phoneNumber: String): Result<ResponseDto<SignInDto>, AppError> {
        return safeCallWrapped(
            call = {
                httpClient.post("api/v1/auth/sign-in") {
                    setBody(SignInParams(phoneNumber))
                }
            }
        )
    }
    
    override suspend fun verifyOtp(
        phoneNumber: String,
        otp: String
    ): Result<ResponseDto<VerifyOtpDto>, AppError> {
        return safeCallWrapped(
            call = {
                httpClient.post("api/v1/auth/otp/verify") {
                    setBody(VerifyOtpParams(phone = phoneNumber, otp))
                }
            }
        )
    }
    
    override suspend fun refreshToken(
        refreshToken: String
    ): Result<ResponseDto<RefreshTokenDto>, AppError> {
        return safeCallWrapped(
            call = {
                httpClient.post("api/v1/auth/refresh-token") {
                    setBody(
                        RefreshTokenParams(
                            refreshToken
                        )
                    )
                }
            }
        )
    }
}