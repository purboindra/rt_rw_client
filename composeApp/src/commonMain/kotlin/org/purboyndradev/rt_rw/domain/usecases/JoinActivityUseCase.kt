package org.purboyndradev.rt_rw.domain.usecases

import org.purboyndradev.rt_rw.core.data.remote.mapper.toActivityError
import org.purboyndradev.rt_rw.core.data.remote.params.JoinActivityParams
import org.purboyndradev.rt_rw.core.domain.ActivityError
import org.purboyndradev.rt_rw.core.domain.Result
import org.purboyndradev.rt_rw.domain.repository.ActivityRepository

class JoinActivityUseCase(private val activityRepository: ActivityRepository) {
    suspend operator fun invoke(params: JoinActivityParams): Result<Unit, ActivityError> {
        return when (val result = activityRepository.joinActivity(params)) {
            is Result.Success -> {
                Result.Success(Unit)
            }
            
            is Result.Error -> {
                Result.Error(result.error.toActivityError())
            }
        }
    }
}