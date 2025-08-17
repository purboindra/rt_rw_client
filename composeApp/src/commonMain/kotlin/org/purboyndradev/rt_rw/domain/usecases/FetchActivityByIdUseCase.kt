package org.purboyndradev.rt_rw.domain.usecases

import org.purboyndradev.rt_rw.core.data.remote.mapper.toActivityDetailModel
import org.purboyndradev.rt_rw.core.domain.AppError
import org.purboyndradev.rt_rw.core.domain.Result
import org.purboyndradev.rt_rw.core.domain.model.ActivityDetailModel
import org.purboyndradev.rt_rw.domain.repository.ActivityRepository

class FetchActivityByIdUseCase(private val activityRepository: ActivityRepository) {
    suspend operator fun invoke(id: String): Result<ActivityDetailModel, AppError> {
        return when (val result = activityRepository.fetchActivityById(id)) {
            is Result.Success -> {
                val data = result.data.data
                
                if (data == null) return Result.Error(AppError.Remote.InvalidResponse)
                Result.Success(data.toActivityDetailModel())
            }
            
            is Result.Error -> {
                val error = result.error
                Result.Error(error)
            }
        }
    }
}