package org.purboyndradev.rt_rw.domain.repository

import org.purboyndradev.rt_rw.core.data.remote.params.RequestEmailVerificationParams
import org.purboyndradev.rt_rw.core.data.remote.params.VerifyEmailParams
import org.purboyndradev.rt_rw.core.domain.AppError
import org.purboyndradev.rt_rw.core.domain.Result

interface UserRepository {

    suspend fun requestEmailVerification(params: RequestEmailVerificationParams): Result<Unit, AppError>
    suspend fun verifyEmail(params: VerifyEmailParams): Result<Unit, AppError>

}