package org.purboyndradev.rt_rw.domain.usecases

import org.purboyndradev.rt_rw.core.domain.AppError
import org.purboyndradev.rt_rw.core.domain.Result
import org.purboyndradev.rt_rw.core.domain.model.ActivityModel
import org.purboyndradev.rt_rw.core.network.mapKtorExceptionToAppError
import org.purboyndradev.rt_rw.domain.repository.ActivityRepository

class FetchActivitiesUseCase(private val activityRepository: ActivityRepository) {
    suspend operator fun invoke(): Result<List<ActivityModel>, AppError> {
        return try {
            val activities = activityRepository.fetchAllActivities()
            Result.Success(activities)
        } catch (e: Exception) {
            Result.Error(mapKtorExceptionToAppError(e))
        }
    }
}