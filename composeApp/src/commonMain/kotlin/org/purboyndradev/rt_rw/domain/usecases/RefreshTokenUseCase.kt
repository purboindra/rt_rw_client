package org.purboyndradev.rt_rw.domain.usecases

import org.purboyndradev.rt_rw.core.domain.AppError
import org.purboyndradev.rt_rw.core.domain.Result
import org.purboyndradev.rt_rw.core.domain.model.RefreshTokenInfo
import org.purboyndradev.rt_rw.core.network.mapKtorExceptionToAppError
import org.purboyndradev.rt_rw.domain.repository.AuthRepository

class RefreshTokenUseCase(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(
        refreshToken: String
    ): Result<RefreshTokenInfo, AppError> {
        return try {
            val result = authRepository.refreshToken(refreshToken)
            val data = result
            val refreshTokenInfo = RefreshTokenInfo(
                refreshToken = data.refreshToken,
                accessToken = data.accessToken,
            )
            Result.Success(refreshTokenInfo)
        } catch (e: Exception) {
            Result.Error(mapKtorExceptionToAppError(e))
        }

    }
}