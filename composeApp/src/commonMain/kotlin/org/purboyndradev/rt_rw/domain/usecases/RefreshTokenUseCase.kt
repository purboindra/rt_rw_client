package org.purboyndradev.rt_rw.domain.usecases

import org.purboyndradev.rt_rw.core.domain.AppError
import org.purboyndradev.rt_rw.core.domain.Result
import org.purboyndradev.rt_rw.core.domain.model.RefreshTokenInfo
import org.purboyndradev.rt_rw.domain.repository.AuthRepository

class RefreshTokenUseCase(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(
        refreshToken: String
    ): Result<RefreshTokenInfo, AppError> {
        return when (val result =
            authRepository.refreshToken(refreshToken)) {
            is Result.Success -> {
                val data = result.data.data
                    ?: return Result.Error(AppError.Remote.InvalidResponse)
                val refreshTokenInfo = RefreshTokenInfo(
                    refreshToken = data.refreshToken,
                    accessToken = data.accessToken,
                )
                Result.Success(refreshTokenInfo)
            }
            
            is Result.Error -> {
                Result.Error(result.error)
            }
        }
    }
}