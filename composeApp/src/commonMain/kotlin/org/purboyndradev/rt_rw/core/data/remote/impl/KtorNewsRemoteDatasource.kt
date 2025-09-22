package org.purboyndradev.rt_rw.core.data.remote.impl

import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.http.appendPathSegments
import org.purboyndradev.rt_rw.core.data.dto.NewsDto
import org.purboyndradev.rt_rw.core.data.dto.ResponseDto
import org.purboyndradev.rt_rw.core.data.remote.api.NewsApi
import org.purboyndradev.rt_rw.core.data.remote.params.PaginationParams
import org.purboyndradev.rt_rw.core.data.remote.params.QueryParams
import org.purboyndradev.rt_rw.core.domain.AppError
import org.purboyndradev.rt_rw.core.domain.Result
import org.purboyndradev.rt_rw.core.network.safeCallWrapped

class KtorNewsRemoteDatasource(private val httpClient: HttpClient) : NewsApi {
    override suspend fun fetchAllNews(
        paginationParams: PaginationParams?,
        queryParams: QueryParams?
    ): Result<ResponseDto<List<NewsDto>>, AppError> {
        return safeCallWrapped(
            call = {
                httpClient.get {
                    url {
                        appendPathSegments("api/v1/news")
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
        )
    }

    override suspend fun fetchNewsById(id: String): Result<ResponseDto<NewsDto>, AppError> {
        return safeCallWrapped(
            call = {
                httpClient.get {
                    url {
                        appendPathSegments("api/v1/news/$id")
                    }
                }
            }
        )
    }
}