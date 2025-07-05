package org.purboyndradev.rt_rw.domain.usecases

import org.purboyndradev.rt_rw.core.data.dto.RefreshTokenDto
import org.purboyndradev.rt_rw.core.data.dto.ResponseDto
import org.purboyndradev.rt_rw.core.domain.DataError
import org.purboyndradev.rt_rw.core.domain.Result
import org.purboyndradev.rt_rw.domain.repository.AuthRepository

class RefreshTokenUseCase(
    private val authRepository: AuthRepository
) {
    suspend fun execute(
        accessToken: String,
        refreshToken: String
    ): Result<ResponseDto<RefreshTokenDto>, DataError.Remote> {
        return authRepository.refreshToken(accessToken, refreshToken)
    }
}