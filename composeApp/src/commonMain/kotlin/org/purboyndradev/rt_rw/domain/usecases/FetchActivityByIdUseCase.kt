package org.purboyndradev.rt_rw.domain.usecases

import org.purboyndradev.rt_rw.core.domain.AppError
import org.purboyndradev.rt_rw.core.domain.Result
import org.purboyndradev.rt_rw.core.domain.model.ActivityDetailModel
import org.purboyndradev.rt_rw.domain.repository.ActivityRepository

class FetchActivityByIdUseCase(private val activityRepository: ActivityRepository) {
    suspend operator fun invoke(id: String): Result<ActivityDetailModel, AppError> {
        return when (val result = activityRepository.fetchActivityById(id)) {
            is Result.Success -> {
                Result.Success(result.data)
            }

            is Result.Error -> {
                Result.Error(result.error)
            }
        }
    }
}