package org.purboyndradev.rt_rw.core.data.remote.impl

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.forms.MultiPartFormDataContent
import io.ktor.client.request.forms.formData
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.Headers
import io.ktor.http.HttpHeaders
import io.ktor.http.appendPathSegments
import org.purboyndradev.rt_rw.core.data.dto.ReportDto
import org.purboyndradev.rt_rw.core.data.dto.ResponseDto
import org.purboyndradev.rt_rw.core.data.remote.api.ReportApi
import org.purboyndradev.rt_rw.core.data.remote.params.CreateReportParams
import org.purboyndradev.rt_rw.core.data.remote.params.PaginationParams
import org.purboyndradev.rt_rw.core.data.remote.params.QueryParams

class KtorReportRemoteDatasource(private val httpClient: HttpClient) : ReportApi {
    override suspend fun fetchAllReports(
        paginationParams: PaginationParams?,
        queryParams: QueryParams?
    ): ResponseDto<List<ReportDto>> {
        return httpClient.get {
            url {
                appendPathSegments("api/v1/reports")
                paginationParams?.let {
                    parameters.append("page", it.page.toString())
                    parameters.append("limit", it.limit.toString())
                }
                queryParams?.let {
                    parameters.append("q", it.query)
                }
            }
        }.body<ResponseDto<List<ReportDto>>>()
    }

    override suspend fun fetchReportById(id: String): ResponseDto<ReportDto> {
        return httpClient.get {
            url {
                appendPathSegments("api/v1/reports/$id")
            }
        }.body<ResponseDto<ReportDto>>()
    }

    override suspend fun createReport(params: CreateReportParams): ResponseDto<Unit> {
        return httpClient.post {
            url {
                appendPathSegments("api/v1/reports")
            }
            setBody(
                MultiPartFormDataContent(
                    formData {
                        append("title", params.title)
                        append("description", params.description)

                        append("image", params.image, Headers.build {
                            append(HttpHeaders.ContentType, "image/jpeg")
                            append(
                                HttpHeaders.ContentDisposition,
                                "filename=\"${params.fileName}\""
                            )
                        })
                    }
                ))
        }.body<ResponseDto<Unit>>()
    }
}