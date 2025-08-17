package org.purboyndradev.rt_rw.domain.usecases

import kotlinx.serialization.json.contentOrNull
import kotlinx.serialization.json.jsonPrimitive
import org.purboyndradev.rt_rw.core.domain.AppError
import org.purboyndradev.rt_rw.core.domain.Result
import org.purboyndradev.rt_rw.core.domain.model.AuthTokenInfo
import org.purboyndradev.rt_rw.domain.repository.AuthRepository
import org.purboyndradev.rt_rw.helper.JWTObject

class SignInUseCase(private val authRepository: AuthRepository) {
    suspend operator fun invoke(phoneNumber: String): Result<AuthTokenInfo, AppError> {
        return when (val result = authRepository.signIn(phoneNumber)) {
            is Result.Error -> Result.Error(AppError.Remote.InvalidResponse)
            is Result.Success -> {
                val user = result.data.data
                
                if (user == null) return Result.Error(AppError.Remote.InvalidResponse)
                
                val code = result.data.code
                val isNotVerified = code == "USER_NOT_VERIFIED"
                
                if (isNotVerified) {
                    return Result.Success(
                        AuthTokenInfo(
                            accessToken = "",
                            refreshToken = "",
                            username = "",
                            email = "",
                            redirectUrl = user.redirectUrl,
                            code = result.data.code
                        )
                    )
                }
                
                val payload = JWTObject.decodeJwtPayload(user.accessToken)
                    ?: return Result.Error(AppError.Remote.InvalidJwt)
                
                val username =
                    payload["username"]?.jsonPrimitive?.contentOrNull ?: ""
                
                val email = payload["email"]?.jsonPrimitive?.contentOrNull ?: ""
                
                Result.Success(
                    AuthTokenInfo(
                        accessToken = user.accessToken,
                        refreshToken = user.refreshToken,
                        username = username,
                        email = email,
                        redirectUrl = user.redirectUrl,
                        code = result.data.code
                    )
                )
            }
        }
    }
}