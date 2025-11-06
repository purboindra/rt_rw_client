package org.purboyndradev.rt_rw.core.data.remote.impl

import io.ktor.client.HttpClient
import io.ktor.client.call.body
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
    ): Result<ResponseDto<List<NewsDto>>, AppError> { // Ensure AppError aligns with safeCallWrapped
        return safeCallWrapped { // Lambda now directly returns the expected type
            httpClient.get {
                url {
                    appendPathSegments("api", "v1", "news") // More robust segment appending
                    paginationParams?.let {
                        parameters.append("page", it.page.toString())
                        parameters.append("limit", it.limit.toString())
                    }
                    queryParams?.let {
                        parameters.append("query", it.query)
                    }
                }
            }.body<ResponseDto<List<NewsDto>>>() // Deserialize to the expected type
        }
    }

    override suspend fun fetchNewsById(id: String): Result<ResponseDto<NewsDto>, AppError> {
        return safeCallWrapped {
            httpClient.get {
                url {
                    // Correctly append individual path segments
                    appendPathSegments("api", "v1", "news", id)
                }
            }.body<ResponseDto<NewsDto>>()
        }
    }
}

