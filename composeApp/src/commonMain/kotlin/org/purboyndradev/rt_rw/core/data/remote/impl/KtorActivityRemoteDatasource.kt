package org.purboyndradev.rt_rw.core.data.remote.impl

import io.ktor.client.HttpClient
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import org.purboyndradev.rt_rw.core.data.dto.ActivityDetailDto
import org.purboyndradev.rt_rw.core.data.dto.ActivityDto
import org.purboyndradev.rt_rw.core.data.dto.ResponseDto
import org.purboyndradev.rt_rw.core.data.remote.api.ActivityApi
import org.purboyndradev.rt_rw.core.data.remote.params.CreateActivityParams
import org.purboyndradev.rt_rw.core.data.remote.params.PaginationParams
import org.purboyndradev.rt_rw.core.data.remote.params.QueryParams
import org.purboyndradev.rt_rw.core.domain.DataError
import org.purboyndradev.rt_rw.core.domain.Result
import org.purboyndradev.rt_rw.core.network.safeCall
import org.purboyndradev.rt_rw.helper.BASE_URL

class KtorActivityRemoteDatasource(private val httpClient: HttpClient) :
    ActivityApi {
    override suspend fun createActivity(params: CreateActivityParams): Result<ResponseDto<Unit>, DataError.Remote> {
        return safeCall {
            httpClient.post("$BASE_URL/activities") {
                setBody(params)
            }
        }
    }
    
    override suspend fun fetchAllActivities(
        paginationParams: PaginationParams?,
        queryParams: QueryParams?
    ): Result<ResponseDto<List<ActivityDto>>, DataError.Remote> {
        return safeCall {
            httpClient.get("$BASE_URL/activities") {
                url {
                    paginationParams?.let {
                        parameters.append("page", it.page.toString())
                        parameters.append("limit", it.limit.toString())
                    }
                    queryParams?.let {
                        parameters.append("query", it.query)
                    }
                }
            }
        }
    }
    
    override suspend fun fetchActivityById(id: String): Result<ResponseDto<ActivityDetailDto>, DataError.Remote> {
        return safeCall {
            httpClient.get("$BASE_URL/activities/${id}")
        }
    }
    
    override suspend fun deleteActivity(id: String): Result<ResponseDto<Unit>, DataError.Remote> {
        return safeCall {
            httpClient.delete("$BASE_URL/activities/${id}")
        }
    }
    
    override suspend fun editActivity(
        id: String,
        params: CreateActivityParams
    ): Result<ResponseDto<Unit>, DataError.Remote> {
        return safeCall {
            httpClient.put("$BASE_URL/activities/${id}") {
                setBody(params)
            }
        }
    }
    
    override suspend fun joinActivity(id: String): Result<ResponseDto<Unit>, DataError.Remote> {
        return safeCall {
            httpClient.post("${BASE_URL}/activities/${id}/join")
        }
    }
}