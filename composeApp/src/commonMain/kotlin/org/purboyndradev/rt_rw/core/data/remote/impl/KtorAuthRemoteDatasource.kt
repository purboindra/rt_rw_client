package org.purboyndradev.rt_rw.core.data.remote.impl

import io.ktor.client.HttpClient
import io.ktor.client.request.post
import org.purboyndradev.rt_rw.core.data.remote.api.AuthApi
import org.purboyndradev.rt_rw.core.domain.DataError
import org.purboyndradev.rt_rw.core.domain.Result
import org.purboyndradev.rt_rw.core.network.safeCall

class KtorAuthRemoteDatasource(private val httpClient: HttpClient) :
    AuthApi {
    override suspend fun signIn(phoneNumber: String): Result<Boolean, DataError.Remote> {
        return safeCall {
            httpClient.post("http://localhost:3000/auth/sign-in") {
                url {
                    parameters.append("phone", phoneNumber)
                }
            }
        }
    }
}