package org.purboyndradev.rt_rw.domain.usecases

import org.purboyndradev.rt_rw.core.data.remote.params.CreateActivityParams
import org.purboyndradev.rt_rw.core.domain.AppError
import org.purboyndradev.rt_rw.core.domain.Result
import org.purboyndradev.rt_rw.core.network.mapKtorExceptionToAppError
import org.purboyndradev.rt_rw.domain.repository.ActivityRepository

class CreateActivityUseCase(private val activityRepository: ActivityRepository) {
    suspend operator fun invoke(
        params: CreateActivityParams
    ): Result<Unit, AppError> {
        return try {
            val response = activityRepository.createActivity(params)
            val data = response.data ?: return Result.Error(AppError.Remote.NotFound)
            Result.Success(data)
        } catch (e: Exception) {
            Result.Error(mapKtorExceptionToAppError(e))
        }
    }
}