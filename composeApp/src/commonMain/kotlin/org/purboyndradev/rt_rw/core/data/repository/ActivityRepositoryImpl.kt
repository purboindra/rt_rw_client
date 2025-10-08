package org.purboyndradev.rt_rw.core.data.repository

import org.purboyndradev.rt_rw.core.data.dto.ActivityDetailDto
import org.purboyndradev.rt_rw.core.data.dto.ActivityDto
import org.purboyndradev.rt_rw.core.data.dto.ResponseDto
import org.purboyndradev.rt_rw.core.data.dto.UsersActivityDto
import org.purboyndradev.rt_rw.core.data.remote.api.ActivityApi
import org.purboyndradev.rt_rw.core.data.remote.params.CreateActivityParams
import org.purboyndradev.rt_rw.core.data.remote.params.JoinActivityParams
import org.purboyndradev.rt_rw.core.data.remote.params.PaginationParams
import org.purboyndradev.rt_rw.core.data.remote.params.QueryParams
import org.purboyndradev.rt_rw.core.domain.AppError
import org.purboyndradev.rt_rw.core.domain.Result
import org.purboyndradev.rt_rw.core.domain.mapBoth
import org.purboyndradev.rt_rw.domain.repository.ActivityRepository

class ActivityRepositoryImpl(
    private val activityApi: ActivityApi
) : ActivityRepository {
    override suspend fun createActivity(params: CreateActivityParams): Result<ResponseDto<Unit>, AppError> {
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
    ): Result<ResponseDto<List<ActivityDto>>, AppError> {
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
    fun fetchActivityById(id: String): Result<ResponseDto<ActivityDetailDto>, AppError> {
        return activityApi.fetchActivityById(id).mapBoth(
            onSuccess = { data -> Result.Success(data) },
            onFailure = { error -> Result.Error(error) }
        )
    }

    override suspend
    fun deleteActivity(id: String): Result<ResponseDto<Unit>, AppError> {
        return activityApi.deleteActivity(id).mapBoth(
            onSuccess = { data -> Result.Success(data) },
            onFailure = { error -> Result.Error(error) }
        )
    }

    override suspend
    fun editActivity(
        id: String,
        params: CreateActivityParams
    ): Result<ResponseDto<Unit>, AppError> {
        return activityApi.editActivity(id, params).mapBoth(
            onSuccess = { data -> Result.Success(data) },
            onFailure = { error -> Result.Error(error) }
        )
    }

    override suspend fun joinActivity(params: JoinActivityParams): Result<ResponseDto<Unit>, AppError> {
        return activityApi.joinActivity(params).mapBoth(
            onSuccess = { data -> Result.Success(data) },
            onFailure = { error -> Result.Error(error) }
        )
    }

    override suspend fun fetchUsersActivity(id: String): Result<ResponseDto<List<UsersActivityDto>>, AppError> {
        return activityApi.fetchUsersActivity(id).mapBoth(
            onSuccess = { data ->
                Result.Success(data)
            },
            onFailure = { error ->
                Result.Error(error)
            }
        )
    }
}