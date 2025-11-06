package org.purboyndradev.rt_rw.domain.usecases

import kotlinx.serialization.json.contentOrNull
import kotlinx.serialization.json.jsonPrimitive
import org.purboyndradev.rt_rw.core.domain.AppError
import org.purboyndradev.rt_rw.core.domain.Result
import org.purboyndradev.rt_rw.core.domain.model.AuthTokenInfo
import org.purboyndradev.rt_rw.core.network.mapKtorExceptionToAppError
import org.purboyndradev.rt_rw.domain.repository.AuthRepository
import org.purboyndradev.rt_rw.helper.JWTObject

class SignInUseCase(private val authRepository: AuthRepository) {
    suspend operator fun invoke(phoneNumber: String): Result<AuthTokenInfo, AppError> {

        return try {

            val result = authRepository.signIn(phoneNumber)
            val user = result.data ?: return Result.Error(
                AppError.Remote.Http(
                    401,
                    result.message
                )
            )

            val code = result.code
            val isNotVerified = code == "USER_NOT_VERIFIED"

            if (isNotVerified) {
                AuthTokenInfo(
                    accessToken = "",
                    refreshToken = "",
                    username = "",
                    email = "",
                    redirectUrl = user.redirectUrl,
                    code = code
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
                    code = code
                )
            )

        } catch (e: Exception) {
            Result.Error(mapKtorExceptionToAppError(e))
        }
    }
}