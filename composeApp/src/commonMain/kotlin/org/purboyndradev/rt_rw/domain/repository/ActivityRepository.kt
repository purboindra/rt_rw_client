package org.purboyndradev.rt_rw.domain.repository

import org.purboyndradev.rt_rw.core.data.dto.ActivityDto
import org.purboyndradev.rt_rw.core.data.dto.ResponseDto
import org.purboyndradev.rt_rw.core.data.remote.params.CreateActivityParams
import org.purboyndradev.rt_rw.core.data.remote.params.PaginationParams
import org.purboyndradev.rt_rw.core.data.remote.params.QueryParams
import org.purboyndradev.rt_rw.core.domain.DataError
import org.purboyndradev.rt_rw.core.domain.Result

interface ActivityRepository {
    suspend fun createActivity(params: CreateActivityParams): org.purboyndradev.rt_rw.core.domain.Result<ResponseDto<Unit>, DataError.Remote>
    suspend fun fetchAllActivities(
        paginationParams: PaginationParams? = null,
        queryParams: QueryParams? = null
    ): org.purboyndradev.rt_rw.core.domain.Result<ResponseDto<List<ActivityDto>>, DataError.Remote>

    suspend fun fetchActivityById(id: String): org.purboyndradev.rt_rw.core.domain.Result<ResponseDto<ActivityDto>, DataError.Remote>
    suspend fun deleteActivity(id: String): org.purboyndradev.rt_rw.core.domain.Result<ResponseDto<Unit>, DataError.Remote>
    suspend fun editActivity(
        id: String,
        params: CreateActivityParams
    ): Result<ResponseDto<Unit>, DataError.Remote>
}