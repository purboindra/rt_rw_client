package org.purboyndradev.rt_rw.core.data.remote.api

import org.purboyndradev.rt_rw.core.data.dto.AuthDto
import org.purboyndradev.rt_rw.core.data.dto.ResponseDto
import org.purboyndradev.rt_rw.core.domain.DataError
import org.purboyndradev.rt_rw.core.domain.Result

interface AuthApi {
    suspend fun signIn(phoneNumber: String): Result<ResponseDto<AuthDto>, DataError.Remote>
}