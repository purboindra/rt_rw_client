package org.purboyndradev.rt_rw.features.auth.domain

import org.purboyndradev.rt_rw.core.domain.AppError
import org.purboyndradev.rt_rw.core.domain.Result
import org.purboyndradev.rt_rw.domain.usecases.SignInUseCase
import org.purboyndradev.rt_rw.domain.usecases.VerifyOtpUseCase

class TelegramOtpVerificationStrategy(
    private val phoneNumber: String,
    private val verifyOtpUseCase: VerifyOtpUseCase,
    private val signInUseCase: SignInUseCase
) : OTPVerificationStrategy {
    override suspend fun verifyOTP(otp: String): Result<Any, AppError> {
        return verifyOtpUseCase(phoneNumber, otp)
    }

    override suspend fun requestOTP(otp: String): Result<Any, AppError> {
        return signInUseCase(otp)
    }
}