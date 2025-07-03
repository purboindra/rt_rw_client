package org.purboyndradev.rt_rw.core.data.remote.api

import org.purboyndradev.rt_rw.core.data.dto.SignInDto
import org.purboyndradev.rt_rw.core.data.dto.ResponseDto
import org.purboyndradev.rt_rw.core.data.dto.VerifyOtpDto
import org.purboyndradev.rt_rw.core.domain.DataError
import org.purboyndradev.rt_rw.core.domain.Result

interface AuthApi {
    suspend fun signIn(phoneNumber: String): Result<ResponseDto<SignInDto>, DataError.Remote>
    suspend fun verifyOtp(
        phoneNumber: String,
        otp: String
    ): Result<ResponseDto<VerifyOtpDto>, DataError.Remote>
}