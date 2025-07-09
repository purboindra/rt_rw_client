package org.purboyndradev.rt_rw.domain.usecases

import org.purboyndradev.rt_rw.core.data.remote.mapper.toActivityError
import org.purboyndradev.rt_rw.core.data.remote.params.CreateActivityParams
import org.purboyndradev.rt_rw.core.domain.ActivityError
import org.purboyndradev.rt_rw.core.domain.Result
import org.purboyndradev.rt_rw.domain.repository.ActivityRepository

class EditActivityUseCase(private val activityRepository: ActivityRepository) {
    suspend operator fun invoke(
        id: String,
        params: CreateActivityParams
    ): Result<Unit, ActivityError> {
        return when (val result = activityRepository.editActivity(id, params)) {
            is Result.Success -> {
                Result.Success(Unit)
            }
            is Result.Error -> {
                Result.Error(result.error.toActivityError())
            }
        }
    }
}