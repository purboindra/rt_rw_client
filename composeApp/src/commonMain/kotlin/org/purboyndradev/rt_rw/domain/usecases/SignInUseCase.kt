package org.purboyndradev.rt_rw.domain.usecases

import kotlinx.serialization.json.contentOrNull
import kotlinx.serialization.json.jsonPrimitive
import org.purboyndradev.rt_rw.core.data.dto.SignInDto
import org.purboyndradev.rt_rw.core.data.dto.ResponseDto
import org.purboyndradev.rt_rw.core.domain.AuthError
import org.purboyndradev.rt_rw.core.domain.DataError
import org.purboyndradev.rt_rw.core.domain.Result
import org.purboyndradev.rt_rw.core.domain.model.AuthTokenInfo
import org.purboyndradev.rt_rw.domain.repository.AuthRepository
import org.purboyndradev.rt_rw.helper.JWTObject

class SignInUseCase(private val authRepository: AuthRepository) {
    suspend operator fun invoke(phoneNumber: String): Result<AuthTokenInfo, AuthError> {
        return when (val result = authRepository.signIn(phoneNumber)) {
            is Result.Error -> Result.Error(AuthError.InvalidResponse)
            is Result.Success -> {
                val user = result.data.data
                
                if (user == null) return Result.Error(AuthError.InvalidResponse)
                
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
                        redirectUrl = user.redirectUrl,
                        code = result.data.code
                    )
                )
            }
        }
    }
}