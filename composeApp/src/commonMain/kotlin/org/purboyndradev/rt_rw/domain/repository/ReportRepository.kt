package org.purboyndradev.rt_rw.domain.repository

import org.purboyndradev.rt_rw.core.data.remote.params.CreateReportParams
import org.purboyndradev.rt_rw.core.data.remote.params.PaginationParams
import org.purboyndradev.rt_rw.core.data.remote.params.QueryParams
import org.purboyndradev.rt_rw.core.domain.model.ReportModel

interface ReportRepository {
    suspend fun createRepository(params: CreateReportParams)
    suspend fun fetchAllReports(
        paginationParams: PaginationParams? = null,
        queryParams: QueryParams? = null
    ): List<ReportModel>

    suspend fun fetchReportById(
        id: String
    ): ReportModel
}