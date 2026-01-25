package org.purboyndradev.rt_rw.domain.usecases

import org.purboyndradev.rt_rw.core.domain.AppError
import org.purboyndradev.rt_rw.core.domain.Result
import org.purboyndradev.rt_rw.core.domain.model.UserModel
import org.purboyndradev.rt_rw.domain.repository.UserRepository

class FetchCurrentUserUseCase(private val repository: UserRepository) {
    suspend operator fun invoke(id: String): Result<UserModel, AppError> {
        return when (val result = repository.fetchCurrentUser(id)) {
            is Result.Success -> {
                Result.Success(result.data)
            }

            is Result.Error -> {
                Result.Error(result.error)
            }
        }
    }
}