package org.purboyndradev.rt_rw.core.data.remote.api

import org.purboyndradev.rt_rw.core.data.dto.RefreshTokenDto
import org.purboyndradev.rt_rw.core.data.dto.ResponseDto
import org.purboyndradev.rt_rw.core.data.dto.SignInDto
import org.purboyndradev.rt_rw.core.data.dto.VerifyOtpDto
import org.purboyndradev.rt_rw.core.domain.AppError
import org.purboyndradev.rt_rw.core.domain.Result

interface AuthApi {
    suspend fun signIn(phoneNumber: String): ResponseDto<SignInDto>
    suspend fun verifyOtp(
        phoneNumber: String,
        otp: String
    ): ResponseDto<VerifyOtpDto>
    
    suspend fun refreshToken(
        refreshToken: String
    ): ResponseDto<RefreshTokenDto>
}