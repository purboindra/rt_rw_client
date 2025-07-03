package org.purboyndradev.rt_rw.core.data.remote.impl

import io.ktor.client.HttpClient
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import org.purboyndradev.rt_rw.core.data.dto.SignInDto
import org.purboyndradev.rt_rw.core.data.dto.ResponseDto
import org.purboyndradev.rt_rw.core.data.remote.api.AuthApi
import org.purboyndradev.rt_rw.core.domain.DataError
import org.purboyndradev.rt_rw.core.domain.Result
import org.purboyndradev.rt_rw.core.network.safeCall
import org.purboyndradev.rt_rw.helper.BASE_URL

data class SignInBody(
    val phone: String
)

class KtorAuthRemoteDatasource(private val httpClient: HttpClient) :
    AuthApi {
    override suspend fun signIn(phoneNumber: String): Result<ResponseDto<SignInDto>, DataError.Remote> {
        return safeCall {
            httpClient.post("$BASE_URL/auth/sign-in") {
                setBody(SignInBody(phoneNumber))
            }
        }
    }
}