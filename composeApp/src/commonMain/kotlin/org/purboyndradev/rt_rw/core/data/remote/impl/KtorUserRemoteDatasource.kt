package org.purboyndradev.rt_rw.core.data.remote.impl

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.appendPathSegments
import org.purboyndradev.rt_rw.core.data.dto.ResponseDto
import org.purboyndradev.rt_rw.core.data.dto.UserDto
import org.purboyndradev.rt_rw.core.data.remote.api.UserApi
import org.purboyndradev.rt_rw.core.data.remote.params.RequestEmailVerificationParams
import org.purboyndradev.rt_rw.core.data.remote.params.VerifyEmailParams

class KtorUserRemoteDatasource(
    private val httpClient: HttpClient
) : UserApi {
    override suspend fun requestEmailVerification(params: RequestEmailVerificationParams): ResponseDto<Unit> {
        return httpClient.post {
            url {
                appendPathSegments("api/v1/users/request-email-verification")
            }
            setBody(params)
        }.body<ResponseDto<Unit>>()
    }

    override suspend fun verifyEmail(
        params: VerifyEmailParams
    ): ResponseDto<Unit> {
        return httpClient.post {
            url {
                appendPathSegments("api/v1/users/verify-email")
            }
            setBody(params)
        }.body<ResponseDto<Unit>>()
    }

    override suspend fun fetchCurrentUser(id: String): ResponseDto<UserDto> {
        return httpClient.get {
            url {
                appendPathSegments("api/v1/users/${id}")
            }
        }.body<ResponseDto<UserDto>>()
    }


}