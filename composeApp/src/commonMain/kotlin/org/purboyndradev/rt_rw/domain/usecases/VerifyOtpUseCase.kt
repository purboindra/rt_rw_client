package org.purboyndradev.rt_rw.domain.usecases

import kotlinx.serialization.json.contentOrNull
import kotlinx.serialization.json.jsonPrimitive
import org.purboyndradev.rt_rw.core.data.dto.ResponseDto
import org.purboyndradev.rt_rw.core.data.dto.VerifyOtpDto
import org.purboyndradev.rt_rw.core.domain.AuthError
import org.purboyndradev.rt_rw.core.domain.DataError
import org.purboyndradev.rt_rw.core.domain.Result
import org.purboyndradev.rt_rw.core.domain.model.AuthTokenInfo
import org.purboyndradev.rt_rw.core.domain.model.RefreshTokenInfo
import org.purboyndradev.rt_rw.domain.repository.AuthRepository
import org.purboyndradev.rt_rw.helper.JWTObject

class VerifyOtpUseCase(private val authRepository: AuthRepository) {
    suspend operator fun invoke(
        phoneNumber: String,
        otp: String
    ): Result<AuthTokenInfo, AuthError> {
        return when (val result = authRepository.verifyOtp(phoneNumber, otp)) {
            is Result.Error -> Result.Error(AuthError.InvalidResponse)
            is Result.Success -> {
                
                val user = result.data.data
                
                if (user == null) return Result.Error(AuthError.InvalidResponse)
                
                val payload = JWTObject.decodeJwtPayload(user.accessToken)
                    ?: return Result.Error(AuthError.InvalidJwt)
                
                val username =
                    payload["username"]?.jsonPrimitive?.contentOrNull ?: ""
                
                val email = payload["email"]?.jsonPrimitive?.contentOrNull ?: ""
                
                Result.Success(
                    AuthTokenInfo(
                        accessToken = user.accessToken,
                        refreshToken = user.refreshToken,
                        username = username,
                        email = email,
                        redirectUrl = null,
                        code = result.data.code
                    )
                )
            }
        }
    }
}