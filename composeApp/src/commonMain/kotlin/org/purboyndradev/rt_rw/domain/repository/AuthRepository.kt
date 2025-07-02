package org.purboyndradev.rt_rw.domain.repository

import org.purboyndradev.rt_rw.core.data.dto.AuthDto
import org.purboyndradev.rt_rw.core.data.dto.ResponseDto
import org.purboyndradev.rt_rw.core.domain.DataError
import org.purboyndradev.rt_rw.core.domain.Result

interface AuthRepository {
    suspend fun signIn(phoneNumber: String): Result<ResponseDto<AuthDto>, DataError.Remote>
}