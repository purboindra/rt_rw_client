package org.purboyndradev.rt_rw.domain.usecases

import org.purboyndradev.rt_rw.core.data.remote.params.RequestEmailVerificationParams
import org.purboyndradev.rt_rw.core.domain.AppError
import org.purboyndradev.rt_rw.core.domain.Result
import org.purboyndradev.rt_rw.core.network.mapKtorExceptionToAppError
import org.purboyndradev.rt_rw.domain.repository.UserRepository

class RequestEmailVerificationUseCase(private val userRepository: UserRepository) {
    suspend operator fun invoke(params: RequestEmailVerificationParams): Result<Unit, AppError> {
        return try {
            userRepository.requestEmailVerification(params)
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(mapKtorExceptionToAppError(e))
        }
    }
}