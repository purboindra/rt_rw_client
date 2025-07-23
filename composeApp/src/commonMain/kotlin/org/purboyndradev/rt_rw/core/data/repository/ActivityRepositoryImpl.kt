package org.purboyndradev.rt_rw.core.data.repository

import org.purboyndradev.rt_rw.core.data.dto.ActivityDetailDto
import org.purboyndradev.rt_rw.core.data.dto.ActivityDto
import org.purboyndradev.rt_rw.core.data.dto.ResponseDto
import org.purboyndradev.rt_rw.core.data.remote.api.ActivityApi
import org.purboyndradev.rt_rw.core.data.remote.params.CreateActivityParams
import org.purboyndradev.rt_rw.core.data.remote.params.PaginationParams
import org.purboyndradev.rt_rw.core.data.remote.params.QueryParams
import org.purboyndradev.rt_rw.core.domain.DataError
import org.purboyndradev.rt_rw.core.domain.Result
import org.purboyndradev.rt_rw.core.domain.mapBoth
import org.purboyndradev.rt_rw.domain.repository.ActivityRepository

class ActivityRepositoryImpl(
    private val activityApi: ActivityApi
) : ActivityRepository {
    override suspend fun createActivity(params: CreateActivityParams): Result<ResponseDto<Unit>, DataError.Remote> {
        return activityApi.createActivity(
            params
        ).mapBoth(
            onSuccess = { data ->
                Result.Success(data)
            },
            onFailure = { error ->
                Result.Error(error)
            }
        )
    }

    override suspend fun fetchAllActivities(
        paginationParams: PaginationParams?,
        queryParams: QueryParams?
    ): Result<ResponseDto<List<ActivityDto>>, DataError.Remote> {
        return activityApi.fetchAllActivities(
            paginationParams, queryParams
        ).mapBoth(
            onSuccess = { data ->
                Result.Success(data)
            },
            onFailure = { error ->
                Result.Error(error)
            }
        )
    }

    override suspend
    fun fetchActivityById(id: String): Result<ResponseDto<ActivityDetailDto>, DataError.Remote> {
        return activityApi.fetchActivityById(id).mapBoth(
            onSuccess = { data -> Result.Success(data) },
            onFailure = { error -> Result.Error(error) }
        )
    }

    override suspend
    fun deleteActivity(id: String): Result<ResponseDto<Unit>, DataError.Remote> {
        return activityApi.deleteActivity(id).mapBoth(
            onSuccess = { data -> Result.Success(data) },
            onFailure = { error -> Result.Error(error) }
        )
    }

    override suspend
    fun editActivity(
        id: String,
        params: CreateActivityParams
    ): Result<ResponseDto<Unit>, DataError.Remote> {
        return activityApi.editActivity(id, params).mapBoth(
            onSuccess = { data -> Result.Success(data) },
            onFailure = { error -> Result.Error(error) }
        )
    }
}