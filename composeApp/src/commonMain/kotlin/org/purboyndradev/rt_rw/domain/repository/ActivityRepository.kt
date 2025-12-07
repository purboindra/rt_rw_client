package org.purboyndradev.rt_rw.domain.repository

import org.purboyndradev.rt_rw.core.data.remote.params.CreateActivityParams
import org.purboyndradev.rt_rw.core.data.remote.params.JoinActivityParams
import org.purboyndradev.rt_rw.core.data.remote.params.PaginationParams
import org.purboyndradev.rt_rw.core.data.remote.params.QueryParams
import org.purboyndradev.rt_rw.core.domain.AppError
import org.purboyndradev.rt_rw.core.domain.Result
import org.purboyndradev.rt_rw.core.domain.model.ActivityDetailModel
import org.purboyndradev.rt_rw.core.domain.model.ActivityModel
import org.purboyndradev.rt_rw.core.domain.model.UsersActivityModel

interface ActivityRepository {
    suspend fun createActivity(params: CreateActivityParams): Result<Unit, AppError>
    suspend fun fetchAllActivities(
        paginationParams: PaginationParams? = null,
        queryParams: QueryParams? = null
    ): Result<List<ActivityModel>, AppError>

    suspend fun fetchActivityById(id: String): Result<ActivityDetailModel, AppError>
    suspend fun deleteActivity(id: String): Result<Unit, AppError>
    suspend fun editActivity(
        id: String,
        params: CreateActivityParams
    ): Result<Unit, AppError>

    suspend fun joinActivity(
        params: JoinActivityParams
    ): Result<Unit, AppError>

    suspend fun fetchUsersActivity(
        id: String
    ): Result<List<UsersActivityModel>, AppError>
}