package org.purboyndradev.rt_rw.core.data.repository

import org.purboyndradev.rt_rw.core.data.datastore.AppAuthRepository
import org.purboyndradev.rt_rw.core.data.remote.api.UserApi
import org.purboyndradev.rt_rw.core.data.remote.mapper.toUserModel
import org.purboyndradev.rt_rw.core.data.remote.params.RequestEmailVerificationParams
import org.purboyndradev.rt_rw.core.data.remote.params.VerifyEmailParams
import org.purboyndradev.rt_rw.core.domain.AppError
import org.purboyndradev.rt_rw.core.domain.Result
import org.purboyndradev.rt_rw.core.domain.model.UserModel
import org.purboyndradev.rt_rw.core.network.mapKtorExceptionToAppError
import org.purboyndradev.rt_rw.domain.repository.UserRepository

class UserRepositoryImpl(
    private val userApi: UserApi,
    private val appAuthRepository: AppAuthRepository
) : UserRepository {
    override suspend fun requestEmailVerification(params: RequestEmailVerificationParams): Result<Unit, AppError> {
        return try {
            userApi.requestEmailVerification(params)
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(mapKtorExceptionToAppError(e))
        }
    }

    override suspend fun verifyEmail(params: VerifyEmailParams): Result<Unit, AppError> {
        return try {
            userApi.verifyEmail(params)
            appAuthRepository.saveEmail(params.email)
            appAuthRepository.saveIsEmailVerified(true)
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(mapKtorExceptionToAppError(e))
        }
    }

    override suspend fun fetchCurrentUser(id: String): Result<UserModel, AppError> {
        return try {
            val response = userApi.fetchCurrentUser(id)
            val data = response.data
            if (data == null) {
                Result.Error(AppError.Remote.NotFound)
            } else {
                Result.Success(data.toUserModel())
            }
        } catch (e: Exception) {
            Result.Error(mapKtorExceptionToAppError(e))
        }
    }
}