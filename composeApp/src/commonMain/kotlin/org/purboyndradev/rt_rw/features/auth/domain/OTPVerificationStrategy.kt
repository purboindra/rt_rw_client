package org.purboyndradev.rt_rw.features.auth.domain

import org.purboyndradev.rt_rw.core.domain.AppError
import org.purboyndradev.rt_rw.core.domain.Result

interface OTPVerificationStrategy {
    suspend fun verifyOTP(otp: String): Result<Any, AppError>
    suspend fun requestOTP(otp: String): Result<Any, AppError>
}