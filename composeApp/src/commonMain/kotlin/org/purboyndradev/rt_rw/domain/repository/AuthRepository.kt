package org.purboyndradev.rt_rw.domain.repository

import org.purboyndradev.rt_rw.core.data.dto.RefreshTokenDto
import org.purboyndradev.rt_rw.core.data.dto.ResponseDto
import org.purboyndradev.rt_rw.core.data.dto.SignInDto
import org.purboyndradev.rt_rw.core.data.dto.VerifyOtpDto
import org.purboyndradev.rt_rw.core.domain.AppError
import org.purboyndradev.rt_rw.core.domain.Result

interface AuthRepository {
    suspend fun signIn(phoneNumber: String): Result<ResponseDto<SignInDto>, AppError>
    suspend fun verifyOtp(
        phoneNumber: String,
        otp: String
    ): Result<ResponseDto<VerifyOtpDto>, AppError>
    
    suspend fun refreshToken(
        refreshToken: String
    ): Result<ResponseDto<RefreshTokenDto>, AppError>
}