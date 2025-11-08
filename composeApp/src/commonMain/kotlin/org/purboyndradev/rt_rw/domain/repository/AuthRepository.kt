package org.purboyndradev.rt_rw.domain.repository

import org.purboyndradev.rt_rw.core.domain.model.RefreshTokenModel
import org.purboyndradev.rt_rw.core.domain.model.SignInModel
import org.purboyndradev.rt_rw.core.domain.model.VerifyOtpModel

interface AuthRepository {
    suspend fun signIn(phoneNumber: String): SignInModel
    suspend fun verifyOtp(
        phoneNumber: String,
        otp: String
    ): VerifyOtpModel
    
    suspend fun refreshToken(
        refreshToken: String
    ): RefreshTokenModel
}