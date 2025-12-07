package org.purboyndradev.rt_rw.domain.usecases

import org.purboyndradev.rt_rw.core.data.remote.params.PaginationParams
import org.purboyndradev.rt_rw.core.data.remote.params.QueryParams
import org.purboyndradev.rt_rw.core.domain.AppError
import org.purboyndradev.rt_rw.core.domain.Result
import org.purboyndradev.rt_rw.core.domain.model.ActivityModel
import org.purboyndradev.rt_rw.domain.repository.ActivityRepository

class FetchActivitiesUseCase(private val activityRepository: ActivityRepository) {
    suspend operator fun invoke(
        queryParams: QueryParams? = null,
        paginationParams: PaginationParams? = null
    ): Result<List<ActivityModel>, AppError> {
        return when (val result = activityRepository.fetchAllActivities(
            queryParams = queryParams,
            paginationParams = paginationParams
        )) {
            is Result.Success -> {
                Result.Success(result.data)
            }

            is Result.Error -> {
                Result.Error(result.error)
            }
        }
    }
}