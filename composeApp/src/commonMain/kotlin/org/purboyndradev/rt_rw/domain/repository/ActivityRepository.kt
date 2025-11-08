package org.purboyndradev.rt_rw.domain.repository

import org.purboyndradev.rt_rw.core.data.remote.params.CreateActivityParams
import org.purboyndradev.rt_rw.core.data.remote.params.JoinActivityParams
import org.purboyndradev.rt_rw.core.data.remote.params.PaginationParams
import org.purboyndradev.rt_rw.core.data.remote.params.QueryParams
import org.purboyndradev.rt_rw.core.domain.model.ActivityDetailModel
import org.purboyndradev.rt_rw.core.domain.model.ActivityModel
import org.purboyndradev.rt_rw.core.domain.model.UsersActivityModel

interface ActivityRepository {
    suspend fun createActivity(params: CreateActivityParams)
    suspend fun fetchAllActivities(
        paginationParams: PaginationParams? = null,
        queryParams: QueryParams? = null
    ): List<ActivityModel>

    suspend fun fetchActivityById(id: String): ActivityDetailModel
    suspend fun deleteActivity(id: String)
    suspend fun editActivity(
        id: String,
        params: CreateActivityParams
    )

    suspend fun joinActivity(
        params: JoinActivityParams
    )

    suspend fun fetchUsersActivity(
        id: String
    ): List<UsersActivityModel>
}