package org.purboyndradev.rt_rw.core.data.repository

import org.purboyndradev.rt_rw.core.data.remote.api.ActivityApi
import org.purboyndradev.rt_rw.core.data.remote.mapper.toActivityDetailModel
import org.purboyndradev.rt_rw.core.data.remote.mapper.toActivityModel
import org.purboyndradev.rt_rw.core.data.remote.mapper.toUsersActivityModel
import org.purboyndradev.rt_rw.core.data.remote.params.CreateActivityParams
import org.purboyndradev.rt_rw.core.data.remote.params.JoinActivityParams
import org.purboyndradev.rt_rw.core.data.remote.params.PaginationParams
import org.purboyndradev.rt_rw.core.data.remote.params.QueryParams
import org.purboyndradev.rt_rw.core.domain.model.ActivityDetailModel
import org.purboyndradev.rt_rw.core.domain.model.ActivityModel
import org.purboyndradev.rt_rw.core.domain.model.UsersActivityModel
import org.purboyndradev.rt_rw.core.network.DataNotFoundException
import org.purboyndradev.rt_rw.domain.repository.ActivityRepository

class ActivityRepositoryImpl(
    private val activityApi: ActivityApi
) : ActivityRepository {
    override suspend fun createActivity(params: CreateActivityParams) {
        activityApi.createActivity(
            params
        )
    }

    override suspend fun fetchAllActivities(
        paginationParams: PaginationParams?,
        queryParams: QueryParams?
    ): List<ActivityModel> {
        val result = activityApi.fetchAllActivities(
            paginationParams, queryParams
        )
        return result.data?.map {
            it.toActivityModel()
        } ?: emptyList()
    }

    override suspend
    fun fetchActivityById(id: String): ActivityDetailModel {
        val result = activityApi.fetchActivityById(id)
        if (result.data != null) {
            return result.data.toActivityDetailModel()
        }
        throw DataNotFoundException("Activity not found")
    }

    override suspend
    fun deleteActivity(id: String) {
        activityApi.deleteActivity(id)
    }

    override suspend
    fun editActivity(
        id: String,
        params: CreateActivityParams
    ) {
        activityApi.editActivity(id, params)
    }

    override suspend fun joinActivity(params: JoinActivityParams) {
        activityApi.joinActivity(params)
    }

    override suspend fun fetchUsersActivity(id: String): List<UsersActivityModel> {
        val result = activityApi.fetchUsersActivity(id)
        return result.data?.map {
            it.toUsersActivityModel()
        } ?: emptyList()
    }
}