package org.purboyndradev.rt_rw.domain.usecases

import org.purboyndradev.rt_rw.core.domain.AppError
import org.purboyndradev.rt_rw.core.domain.Result
import org.purboyndradev.rt_rw.core.domain.model.ActivityDetailModel
import org.purboyndradev.rt_rw.core.network.mapKtorExceptionToAppError
import org.purboyndradev.rt_rw.domain.repository.ActivityRepository

class FetchActivityByIdUseCase(private val activityRepository: ActivityRepository) {
    suspend operator fun invoke(id: String): Result<ActivityDetailModel, AppError> {
        return try {
            val activity = activityRepository.fetchActivityById(id)
            Result.Success(activity)
        } catch (e: Exception) {
            Result.Error(mapKtorExceptionToAppError(e))
        }
    }
}