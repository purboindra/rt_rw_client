package org.purboyndradev.rt_rw.core.data.repository

import org.purboyndradev.rt_rw.core.data.dto.RefreshTokenDto
import org.purboyndradev.rt_rw.core.data.dto.SignInDto
import org.purboyndradev.rt_rw.core.data.dto.ResponseDto
import org.purboyndradev.rt_rw.core.data.dto.VerifyOtpDto
import org.purboyndradev.rt_rw.core.data.remote.api.AuthApi
import org.purboyndradev.rt_rw.core.domain.DataError
import org.purboyndradev.rt_rw.core.domain.Result
import org.purboyndradev.rt_rw.domain.repository.AuthRepository

class AuthRepositoryImpl(
    private val api: AuthApi
) : AuthRepository {
    override suspend fun signIn(phoneNumber: String): Result<ResponseDto<SignInDto>, DataError.Remote> {
        val response = api.signIn(phoneNumber)
        return response
    }
    
    override suspend fun verifyOtp(
        phoneNumber: String,
        otp: String
    ): Result<ResponseDto<VerifyOtpDto>, DataError.Remote> {
        val response = api.verifyOtp(phoneNumber, otp)
        return response
    }
    
    override suspend fun refreshToken(
        accessToken: String,
        refreshToken: String
    ): Result<ResponseDto<RefreshTokenDto>, DataError.Remote> {
        val response = api.refreshToken(accessToken, refreshToken)
        return response
    }
}