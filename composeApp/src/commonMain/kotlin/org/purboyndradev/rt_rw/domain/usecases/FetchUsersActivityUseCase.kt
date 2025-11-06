package org.purboyndradev.rt_rw.domain.usecases

import org.purboyndradev.rt_rw.core.data.remote.mapper.toUsersActivityModel
import org.purboyndradev.rt_rw.core.domain.AppError
import org.purboyndradev.rt_rw.core.domain.Result
import org.purboyndradev.rt_rw.core.domain.model.UsersActivityModel
import org.purboyndradev.rt_rw.core.network.mapKtorExceptionToAppError
import org.purboyndradev.rt_rw.domain.repository.ActivityRepository

class FetchUsersActivityUseCase(private val activityRepository: ActivityRepository) {
    suspend operator fun invoke(id: String): Result<List<UsersActivityModel>, AppError> {
        return try {
            val result = activityRepository.fetchUsersActivity(id)
            val data = result.data ?: return Result.Error(AppError.Remote.NotFound)
            val usersActivity = data.map {
                it.toUsersActivityModel()
            }
            Result.Success(usersActivity)
        } catch (e: Exception) {
            Result.Error(mapKtorExceptionToAppError(e))
        }
    }
}