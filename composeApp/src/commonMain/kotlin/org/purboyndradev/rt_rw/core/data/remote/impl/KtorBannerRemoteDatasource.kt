package org.purboyndradev.rt_rw.core.data.remote.impl

import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.http.appendPathSegments
import io.ktor.http.parameters
import org.purboyndradev.rt_rw.core.data.dto.BannerDto
import org.purboyndradev.rt_rw.core.data.dto.ResponseDto
import org.purboyndradev.rt_rw.core.data.remote.api.BannerApi
import org.purboyndradev.rt_rw.core.data.remote.params.PaginationParams
import org.purboyndradev.rt_rw.core.data.remote.params.QueryParams
import org.purboyndradev.rt_rw.core.domain.AppError
import org.purboyndradev.rt_rw.core.domain.Result
import org.purboyndradev.rt_rw.core.network.safeCallWrapped

class KtorBannerRemoteDatasource(private val httpClient: HttpClient) :
    BannerApi {
    override suspend fun fetchAllBanners(
        paginationParams: PaginationParams?,
        queryParams: QueryParams?
    ): Result<ResponseDto<List<BannerDto>>, AppError> {
        return safeCallWrapped(
            call = {
                httpClient.get {
                    url {
                        paginationParams?.let {
                            parameters.append("page", it.page.toString())
                            parameters.append("limit", it.limit.toString())
                        }
                        queryParams?.let {
                            parameters.append("query", it.query)
                        }
                        appendPathSegments("api/v1/banners")
                    }
                }
            }
        )
    }
}