package org.purboyndradev.rt_rw.core.data.remote.api

import org.purboyndradev.rt_rw.core.data.dto.ActivityDetailDto
import org.purboyndradev.rt_rw.core.data.dto.ActivityDto
import org.purboyndradev.rt_rw.core.data.dto.ResponseDto
import org.purboyndradev.rt_rw.core.data.dto.UsersActivityWrapperDto
import org.purboyndradev.rt_rw.core.data.remote.params.CreateActivityParams
import org.purboyndradev.rt_rw.core.data.remote.params.JoinActivityParams
import org.purboyndradev.rt_rw.core.data.remote.params.PaginationParams
import org.purboyndradev.rt_rw.core.data.remote.params.QueryParams

interface ActivityApi {
    suspend fun createActivity(params: CreateActivityParams): ResponseDto<Unit>
    suspend fun fetchAllActivities(
        paginationParams: PaginationParams? = null,
        queryParams: QueryParams? = null
    ): ResponseDto<List<ActivityDto>>

    suspend fun fetchActivityById(id: String): ResponseDto<ActivityDetailDto>
    suspend fun deleteActivity(id: String): ResponseDto<Unit>
    suspend fun editActivity(
        id: String,
        params: CreateActivityParams
    ): ResponseDto<Unit>

    suspend fun joinActivity(params: JoinActivityParams): ResponseDto<Unit>
    suspend fun fetchUsersActivity(id: String): ResponseDto<UsersActivityWrapperDto>
}