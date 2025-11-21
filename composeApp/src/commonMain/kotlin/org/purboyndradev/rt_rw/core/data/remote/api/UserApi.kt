package org.purboyndradev.rt_rw.core.data.remote.api

import org.purboyndradev.rt_rw.core.data.dto.ResponseDto
import org.purboyndradev.rt_rw.core.data.remote.params.RequestEmailVerificationParams
import org.purboyndradev.rt_rw.core.data.remote.params.VerifyEmailParams

interface UserApi {

    suspend fun requestEmailVerification(params: RequestEmailVerificationParams): ResponseDto<Unit>
    suspend fun verifyEmail(params: VerifyEmailParams): ResponseDto<Unit>

}