package org.purboyndradev.rt_rw.domain.usecases

import org.purboyndradev.rt_rw.core.domain.AppError
import org.purboyndradev.rt_rw.core.domain.Result
import org.purboyndradev.rt_rw.domain.repository.ActivityRepository

class DeleteActivityUseCase(private val activityRepository: ActivityRepository) {
    suspend operator fun invoke(id: String): Result<Unit, AppError> {
        return when (val result = activityRepository.deleteActivity(id)) {
            is Result.Success -> {
                Result.Success(Unit)
            }
            
            is Result.Error -> {
                val error = result.error
                Result.Error(error)
            }
        }
    }
}