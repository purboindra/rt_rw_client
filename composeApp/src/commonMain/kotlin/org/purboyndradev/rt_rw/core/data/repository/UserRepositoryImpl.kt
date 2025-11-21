package org.purboyndradev.rt_rw.core.data.repository

import org.purboyndradev.rt_rw.core.data.remote.api.UserApi
import org.purboyndradev.rt_rw.core.data.remote.params.RequestEmailVerificationParams
import org.purboyndradev.rt_rw.core.data.remote.params.VerifyEmailParams
import org.purboyndradev.rt_rw.domain.repository.UserRepository

class UserRepositoryImpl(private val userApi: UserApi) : UserRepository {
    override suspend fun requestEmailVerification(params: RequestEmailVerificationParams) {
        userApi.requestEmailVerification(params)
    }

    override suspend fun verifyEmail(params: VerifyEmailParams) {
        userApi.verifyEmail(params)
    }
}