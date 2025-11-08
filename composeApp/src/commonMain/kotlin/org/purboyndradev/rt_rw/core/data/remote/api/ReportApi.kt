package org.purboyndradev.rt_rw.core.data.remote.api

import org.purboyndradev.rt_rw.core.data.dto.ReportDto
import org.purboyndradev.rt_rw.core.data.dto.ResponseDto
import org.purboyndradev.rt_rw.core.data.remote.params.CreateReportParams
import org.purboyndradev.rt_rw.core.data.remote.params.PaginationParams
import org.purboyndradev.rt_rw.core.data.remote.params.QueryParams

interface ReportApi {
    suspend fun fetchAllReports(
        paginationParams: PaginationParams? = null,
        queryParams: QueryParams? = null
    ): ResponseDto<List<ReportDto>>

    suspend fun fetchReportById(
        id: String
    ): ResponseDto<ReportDto>

    suspend fun createReport(
        params: CreateReportParams
    ): ResponseDto<Unit>
}