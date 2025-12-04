package org.purboyndradev.rt_rw.features.auth.domain

import org.purboyndradev.rt_rw.core.data.remote.params.RequestEmailVerificationParams
import org.purboyndradev.rt_rw.core.data.remote.params.VerifyEmailParams
import org.purboyndradev.rt_rw.core.domain.AppError
import org.purboyndradev.rt_rw.core.domain.Result
import org.purboyndradev.rt_rw.domain.usecases.RequestEmailVerificationUseCase
import org.purboyndradev.rt_rw.domain.usecases.VerifyEmailUseCase

class EmailOtpVerificationStrategy(
    private val email: String,
    private val verifyEmailUseCase: VerifyEmailUseCase,
    private val requestEmailVerificationUseCase: RequestEmailVerificationUseCase,
) : OTPVerificationStrategy {
    override suspend fun verifyOTP(otp: String): Result<Any, AppError> {
        return verifyEmailUseCase(
            VerifyEmailParams(
                code = otp,
                email = email
            )
        )
    }

    override suspend fun requestOTP(otp: String): Result<Any, AppError> {
        return requestEmailVerificationUseCase(
            RequestEmailVerificationParams(
                email = email
            )
        )
    }
}