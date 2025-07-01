package org.purboyndradev.rt_rw.domain.usecases

import org.purboyndradev.rt_rw.core.domain.DataError
import org.purboyndradev.rt_rw.core.domain.Result
import org.purboyndradev.rt_rw.domain.repository.AuthRepository

class SignInUseCase(private val authRepository: AuthRepository) {
    suspend fun execute(phoneNumber: String): Result<Boolean, DataError.Remote> {
        return authRepository.signIn(phoneNumber)
    }
}