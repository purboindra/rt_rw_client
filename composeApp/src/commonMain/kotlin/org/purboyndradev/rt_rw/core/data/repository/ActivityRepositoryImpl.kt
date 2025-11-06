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
    override suspend fun createActivity(params: CreateActivityParams): ResponseDto<Unit> {
        return activityApi.createActivity(
            params
        )
    }

    override suspend fun fetchAllActivities(
        paginationParams: PaginationParams?,
        queryParams: QueryParams?
    ): ResponseDto<List<ActivityDto>> {
        return activityApi.fetchAllActivities(
            paginationParams, queryParams
        )
    }

    override suspend
    fun fetchActivityById(id: String): ResponseDto<ActivityDetailDto> {
        return activityApi.fetchActivityById(id)
    }

    override suspend
    fun deleteActivity(id: String): ResponseDto<Unit> {
        return activityApi.deleteActivity(id)
    }

    override suspend
    fun editActivity(
        id: String,
        params: CreateActivityParams
    ): ResponseDto<Unit> {
        return activityApi.editActivity(id, params)
    }

    override suspend fun joinActivity(params: JoinActivityParams): ResponseDto<Unit> {
        return activityApi.joinActivity(params)
    }

    override suspend fun fetchUsersActivity(id: String): ResponseDto<List<UsersActivityDto>> {
        return activityApi.fetchUsersActivity(id)
    }
}