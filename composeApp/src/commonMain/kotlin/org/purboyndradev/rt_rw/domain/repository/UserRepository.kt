package org.purboyndradev.rt_rw.domain.repository

import org.purboyndradev.rt_rw.core.data.remote.params.RequestEmailVerificationParams
import org.purboyndradev.rt_rw.core.data.remote.params.VerifyEmailParams

interface UserRepository {

    suspend fun requestEmailVerification(params: RequestEmailVerificationParams): Unit
    suspend fun verifyEmail(params: VerifyEmailParams): Unit

}