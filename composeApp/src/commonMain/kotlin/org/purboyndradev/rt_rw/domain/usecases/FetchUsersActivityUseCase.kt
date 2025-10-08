package org.purboyndradev.rt_rw.domain.usecases

import org.purboyndradev.rt_rw.core.data.remote.mapper.toUsersActivityModel
import org.purboyndradev.rt_rw.core.domain.AppError
import org.purboyndradev.rt_rw.core.domain.Result
import org.purboyndradev.rt_rw.core.domain.model.UsersActivityModel
import org.purboyndradev.rt_rw.domain.repository.ActivityRepository

class FetchUsersActivityUseCase(private val activityRepository: ActivityRepository) {
    suspend operator fun invoke(id: String): Result<List<UsersActivityModel>, AppError> {
        return when (val result = activityRepository.fetchUsersActivity(id)) {
            is Result.Success -> {
                val data = result.data.data
                if (data == null) return Result.Error(AppError.Remote.NotFound)
                var usersActivity: List<UsersActivityModel> = emptyList()
                data.forEach {
                    usersActivity = usersActivity + it.toUsersActivityModel()
                }
                Result.Success(usersActivity)
            }

            is Result.Error -> {
                val error = result.error
                Result.Error(error)
            }
        }
    }
}