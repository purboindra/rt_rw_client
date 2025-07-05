package org.purboyndradev.rt_rw.domain.repository

import org.purboyndradev.rt_rw.core.data.dto.RefreshTokenDto
import org.purboyndradev.rt_rw.core.data.dto.SignInDto
import org.purboyndradev.rt_rw.core.data.dto.ResponseDto
import org.purboyndradev.rt_rw.core.data.dto.VerifyOtpDto
import org.purboyndradev.rt_rw.core.domain.DataError
import org.purboyndradev.rt_rw.core.domain.Result

interface AuthRepository {
    suspend fun signIn(phoneNumber: String): Result<ResponseDto<SignInDto>, DataError.Remote>
    suspend fun verifyOtp(
        phoneNumber: String,
        otp: String
    ): Result<ResponseDto<VerifyOtpDto>, DataError.Remote>
    
    suspend fun refreshToken(
        accessToken: String,
        refreshToken: String
    ): Result<ResponseDto<RefreshTokenDto>, DataError.Remote>
}