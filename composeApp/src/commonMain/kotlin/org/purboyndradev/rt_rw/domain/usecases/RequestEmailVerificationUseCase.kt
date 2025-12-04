package org.purboyndradev.rt_rw.domain.usecases

import org.purboyndradev.rt_rw.core.data.remote.params.RequestEmailVerificationParams
import org.purboyndradev.rt_rw.core.domain.AppError
import org.purboyndradev.rt_rw.core.domain.Result
import org.purboyndradev.rt_rw.domain.repository.UserRepository

class RequestEmailVerificationUseCase(private val userRepository: UserRepository) {
    suspend operator fun invoke(params: RequestEmailVerificationParams): Result<Unit, AppError> {
        return when(val result = userRepository.requestEmailVerification(params)){
            is Result.Success -> {
                Result.Success(Unit)
            }
            is Result.Error -> {
                Result.Error(result.error)
            }
        }
    }
}