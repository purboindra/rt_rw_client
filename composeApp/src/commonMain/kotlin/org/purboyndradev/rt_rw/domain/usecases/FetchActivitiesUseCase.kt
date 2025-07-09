package org.purboyndradev.rt_rw.domain.usecases

import org.purboyndradev.rt_rw.core.data.remote.mapper.toActivityError
import org.purboyndradev.rt_rw.core.data.remote.mapper.toActivityModel
import org.purboyndradev.rt_rw.core.domain.ActivityError
import org.purboyndradev.rt_rw.core.domain.Result
import org.purboyndradev.rt_rw.core.domain.model.ActivityModel
import org.purboyndradev.rt_rw.domain.repository.ActivityRepository

class FetchActivitiesUseCase(private val activityRepository: ActivityRepository) {
    suspend operator fun invoke(): Result<List<ActivityModel>, ActivityError> {
        return when (val result = activityRepository.fetchAllActivities()) {
            is Result.Success -> {
                val data = result.data.data
                if (data == null) return Result.Error(ActivityError.InvalidResponse)
                var activities: List<ActivityModel> = emptyList()
                data.forEach {
                    activities = activities + it.toActivityModel()
                }
                Result.Success(activities)
            }

            is Result.Error -> {
                val error = result.error
                Result.Error(error.toActivityError())
            }
        }
    }
}