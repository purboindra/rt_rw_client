package org.purboyndradev.rt_rw.domain.usecases

import org.purboyndradev.rt_rw.core.data.dto.RefreshTokenDto
import org.purboyndradev.rt_rw.core.data.dto.ResponseDto
import org.purboyndradev.rt_rw.core.domain.AuthError
import org.purboyndradev.rt_rw.core.domain.DataError
import org.purboyndradev.rt_rw.core.domain.Result
import org.purboyndradev.rt_rw.core.domain.model.RefreshTokenInfo
import org.purboyndradev.rt_rw.domain.repository.AuthRepository

class RefreshTokenUseCase(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(
        accessToken: String,
        refreshToken: String
    ): Result<RefreshTokenInfo, AuthError> {
        return when (val result =
            authRepository.refreshToken(accessToken, refreshToken)) {
            is Result.Success -> {
                val data = result.data.data
                    ?: return Result.Error(AuthError.InvalidResponse)
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