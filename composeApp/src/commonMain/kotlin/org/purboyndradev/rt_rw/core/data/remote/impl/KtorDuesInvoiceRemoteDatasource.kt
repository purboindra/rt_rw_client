package org.purboyndradev.rt_rw.core.data.remote.impl

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.http.appendPathSegments
import org.purboyndradev.rt_rw.core.data.dto.DuesInvoiceDto
import org.purboyndradev.rt_rw.core.data.dto.ResponseDto
import org.purboyndradev.rt_rw.core.data.remote.api.DuesInvoiceApi
import org.purboyndradev.rt_rw.core.data.remote.params.PaginationParams
import org.purboyndradev.rt_rw.core.data.remote.params.QueryParams

class KtorDuesInvoiceRemoteDatasource(private val httpClient: HttpClient) : DuesInvoiceApi {
    override suspend fun fetchAllDuesInvoices(
        paginationParams: PaginationParams?,
        queryParams: QueryParams?
    ): ResponseDto<List<DuesInvoiceDto>> {
        return httpClient.get {
            url {
                appendPathSegments("api/v1/me/dues-invoices")
                paginationParams?.let {
                    parameters.append("page", it.page.toString())
                    parameters.append("limit", it.limit.toString())
                }
                queryParams?.let {
                    parameters.append("q", it.query)
                }
            }
        }.body<ResponseDto<List<DuesInvoiceDto>>>()
    }

    override suspend fun fetchDuesInvoiceById(id: String): ResponseDto<DuesInvoiceDto> {
        return httpClient.get {
            url {
                appendPathSegments("api/v1/me/dues-invoices/${id}")
            }
        }.body<ResponseDto<DuesInvoiceDto>>()
    }
}