package org.purboyndradev.rt_rw.domain.usecases

import org.purboyndradev.rt_rw.core.data.dto.ResponseDto
import org.purboyndradev.rt_rw.core.data.dto.VerifyOtpDto
import org.purboyndradev.rt_rw.core.domain.DataError
import org.purboyndradev.rt_rw.core.domain.Result
import org.purboyndradev.rt_rw.domain.repository.AuthRepository

class VerifyOtpUseCase(private val authRepository: AuthRepository) {
    suspend fun execute(
        phoneNumber: String,
        otp: String
    ): Result<ResponseDto<VerifyOtpDto>, DataError.Remote> {
        return authRepository.verifyOtp(phoneNumber, otp)
    }
}