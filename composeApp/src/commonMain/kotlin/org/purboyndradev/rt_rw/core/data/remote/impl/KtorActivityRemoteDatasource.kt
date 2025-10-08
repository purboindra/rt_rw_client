package org.purboyndradev.rt_rw.core.data.remote.impl

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.http.appendPathSegments
import io.ktor.http.cio.Response
import org.purboyndradev.rt_rw.core.data.dto.ActivityDetailDto
import org.purboyndradev.rt_rw.core.data.dto.ActivityDto
import org.purboyndradev.rt_rw.core.data.dto.ResponseDto
import org.purboyndradev.rt_rw.core.data.dto.UsersActivityDto
import org.purboyndradev.rt_rw.core.data.remote.api.ActivityApi
import org.purboyndradev.rt_rw.core.data.remote.params.CreateActivityParams
import org.purboyndradev.rt_rw.core.data.remote.params.JoinActivityParams
import org.purboyndradev.rt_rw.core.data.remote.params.PaginationParams
import org.purboyndradev.rt_rw.core.data.remote.params.QueryParams
import org.purboyndradev.rt_rw.core.domain.AppError
import org.purboyndradev.rt_rw.core.domain.Result
import org.purboyndradev.rt_rw.core.network.safeCallWrapped

class KtorActivityRemoteDatasource(private val httpClient: HttpClient) :
    ActivityApi {
    override suspend fun createActivity(params: CreateActivityParams): Result<ResponseDto<Unit>, AppError> {
        return safeCallWrapped {
            httpClient.post {
                url {
                    appendPathSegments("api/v1/activities")
                }
                setBody(params)
            }.body<ResponseDto<Unit>>()
        }
    }

    override suspend fun fetchAllActivities(
        paginationParams: PaginationParams?,
        queryParams: QueryParams?
    ): Result<ResponseDto<List<ActivityDto>>, AppError> {
        return safeCallWrapped {
            httpClient.get {
                url {
                    appendPathSegments("api/v1/activities")
                    paginationParams?.let {
                        parameters.append("page", it.page.toString())
                        parameters.append("limit", it.limit.toString())
                    }
                    queryParams?.let {
                        parameters.append("query", it.query)
                    }
                }
            }.body<ResponseDto<List<ActivityDto>>>()
        }
    }

    override suspend fun fetchActivityById(id: String): Result<ResponseDto<ActivityDetailDto>, AppError> {
        return safeCallWrapped {
            httpClient.get {
                url {
                    appendPathSegments("api/v1/activities/$id")
                }
            }.body<ResponseDto<ActivityDetailDto>>()
        }

    }

    override suspend fun deleteActivity(id: String): Result<ResponseDto<Unit>, AppError> {
        return safeCallWrapped {
            httpClient.delete {
                url {
                    appendPathSegments("api/v1/activities/$id")
                }
            }.body<ResponseDto<Unit>>()
        }
    }

    override suspend fun editActivity(
        id: String,
        params: CreateActivityParams
    ): Result<ResponseDto<Unit>, AppError> {
        return safeCallWrapped {
            httpClient.put {
                url {
                    appendPathSegments("api/v1/activities/$id")
                }
                setBody(params)
            }.body<ResponseDto<Unit>>()
        }
    }

    override suspend fun joinActivity(params: JoinActivityParams): Result<ResponseDto<Unit>, AppError> {
        return safeCallWrapped {
            httpClient.post {
                url {
                    appendPathSegments("api/v1/activities/join")
                }
                setBody(
                    params
                )
            }.body<ResponseDto<Unit>>()
        }
    }

    override suspend fun fetchUsersActivity(id: String): Result<ResponseDto<List<UsersActivityDto>>, AppError> {
        return safeCallWrapped {
            httpClient.get {
                url {
                    appendPathSegments("api/v1/activities/${id}/users")
                }
            }.body<ResponseDto<List<UsersActivityDto>>>()
        }
    }
}