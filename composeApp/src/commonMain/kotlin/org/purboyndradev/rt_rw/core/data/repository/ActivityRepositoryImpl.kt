package org.purboyndradev.rt_rw.core.data.repository

import org.purboyndradev.rt_rw.core.data.remote.api.ActivityApi
import org.purboyndradev.rt_rw.core.data.remote.mapper.toActivityDetailModel
import org.purboyndradev.rt_rw.core.data.remote.mapper.toActivityModel
import org.purboyndradev.rt_rw.core.data.remote.mapper.toUsersActivityModel
import org.purboyndradev.rt_rw.core.data.remote.params.CreateActivityParams
import org.purboyndradev.rt_rw.core.data.remote.params.JoinActivityParams
import org.purboyndradev.rt_rw.core.data.remote.params.PaginationParams
import org.purboyndradev.rt_rw.core.data.remote.params.QueryParams
import org.purboyndradev.rt_rw.core.domain.AppError
import org.purboyndradev.rt_rw.core.domain.Result
import org.purboyndradev.rt_rw.core.domain.model.ActivityDetailModel
import org.purboyndradev.rt_rw.core.domain.model.ActivityModel
import org.purboyndradev.rt_rw.core.domain.model.UsersActivityModel
import org.purboyndradev.rt_rw.core.network.DataNotFoundException
import org.purboyndradev.rt_rw.core.network.mapKtorExceptionToAppError
import org.purboyndradev.rt_rw.domain.repository.ActivityRepository

class ActivityRepositoryImpl(
    private val activityApi: ActivityApi
) : ActivityRepository {
    override suspend fun createActivity(params: CreateActivityParams): Result<Unit, AppError> {
        return try {
            activityApi.createActivity(
                params
            )
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(mapKtorExceptionToAppError(e))
        }
    }

    override suspend fun fetchAllActivities(
        paginationParams: PaginationParams?, queryParams: QueryParams?
    ): Result<List<ActivityModel>, AppError> {
        return try {
            val result = activityApi.fetchAllActivities(
                paginationParams, queryParams
            )
            val activities = result.data?.map {
                it.toActivityModel()
            } ?: emptyList()
            Result.Success(activities)
        } catch (e: Exception) {
            Result.Error(mapKtorExceptionToAppError(e))
        }
    }

    override suspend fun fetchActivityById(id: String): Result<ActivityDetailModel, AppError> {
        return try {
            val result = activityApi.fetchActivityById(id)
            if (result.data == null) {
                throw DataNotFoundException("Activity not found")
            }
            Result.Success(result.data.toActivityDetailModel())
        } catch (e: Exception) {
            Result.Error(mapKtorExceptionToAppError(e))
        }
    }

    override suspend fun deleteActivity(id: String): Result<Unit, AppError> {
        return try {
            activityApi.deleteActivity(id)
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(mapKtorExceptionToAppError(e))
        }
    }

    override suspend fun editActivity(
        id: String, params: CreateActivityParams
    ): Result<Unit, AppError> {
        return try {
            activityApi.editActivity(id, params)
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(mapKtorExceptionToAppError(e))
        }
    }

    override suspend fun joinActivity(params: JoinActivityParams): Result<Unit, AppError> {
        return try {
            activityApi.joinActivity(params)
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(mapKtorExceptionToAppError(e))
        }
    }

    override suspend fun fetchUsersActivity(id: String): Result<List<UsersActivityModel>, AppError> {
        return try {
            val result = activityApi.fetchUsersActivity(id)
            val userActivities = result.data?.users?.map {
                it.toUsersActivityModel()
            } ?: emptyList()
            Result.Success(userActivities)
        } catch (e: Exception) {
            Result.Error(mapKtorExceptionToAppError(e))
        }
    }
}