package org.purboyndradev.rt_rw.core.data.remote.api

import org.purboyndradev.rt_rw.core.data.dto.ActivityDetailDto
import org.purboyndradev.rt_rw.core.data.dto.ActivityDto
import org.purboyndradev.rt_rw.core.data.dto.ResponseDto
import org.purboyndradev.rt_rw.core.data.remote.params.CreateActivityParams
import org.purboyndradev.rt_rw.core.data.remote.params.JoinActivityParams
import org.purboyndradev.rt_rw.core.data.remote.params.PaginationParams
import org.purboyndradev.rt_rw.core.data.remote.params.QueryParams
import org.purboyndradev.rt_rw.core.domain.DataError
import org.purboyndradev.rt_rw.core.domain.Result

interface ActivityApi {
    suspend fun createActivity(params: CreateActivityParams): Result<ResponseDto<Unit>, DataError.Remote>
    suspend fun fetchAllActivities(
        paginationParams: PaginationParams? = null,
        queryParams: QueryParams? = null
    ): Result<ResponseDto<List<ActivityDto>>, DataError.Remote>
    
    suspend fun fetchActivityById(id: String): Result<ResponseDto<ActivityDetailDto>, DataError.Remote>
    suspend fun deleteActivity(id: String): Result<ResponseDto<Unit>, DataError.Remote>
    suspend fun editActivity(
        id: String,
        params: CreateActivityParams
    ): Result<ResponseDto<Unit>, DataError.Remote>
    
    suspend fun joinActivity(params: JoinActivityParams): Result<ResponseDto<Unit>, DataError.Remote>
}