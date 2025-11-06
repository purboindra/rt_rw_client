package org.purboyndradev.rt_rw.domain.usecases

import org.purboyndradev.rt_rw.core.data.remote.params.JoinActivityParams
import org.purboyndradev.rt_rw.core.domain.AppError
import org.purboyndradev.rt_rw.core.domain.Result
import org.purboyndradev.rt_rw.core.network.mapKtorExceptionToAppError
import org.purboyndradev.rt_rw.domain.repository.ActivityRepository

class JoinActivityUseCase(private val activityRepository: ActivityRepository) {
    suspend operator fun invoke(params: JoinActivityParams): Result<Unit, AppError> {
        return try {
            val result = activityRepository.joinActivity(params)
            val data = result.data ?: return Result.Error(AppError.Remote.NotFound)
            Result.Success(data)
        } catch (e: Exception) {
            Result.Error(mapKtorExceptionToAppError(e))
        }
    }
}