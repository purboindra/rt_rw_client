package org.purboyndradev.rt_rw.domain.usecases

import org.purboyndradev.rt_rw.core.data.remote.mapper.toActivityError
import org.purboyndradev.rt_rw.core.data.remote.params.CreateActivityParams
import org.purboyndradev.rt_rw.core.domain.ActivityError
import org.purboyndradev.rt_rw.core.domain.Result
import org.purboyndradev.rt_rw.domain.repository.ActivityRepository

class CreateActivityUseCase(private val activityRepository: ActivityRepository) {
    suspend operator fun invoke(
        params: CreateActivityParams
    ): Result<Unit, ActivityError> {
        return when (val result = activityRepository.createActivity(params)) {
            is Result.Success -> {
                Result.Success(Unit)
            }

            is Result.Error -> {
                val error = result.error
                Result.Error(error.toActivityError())
            }
        }
    }
}