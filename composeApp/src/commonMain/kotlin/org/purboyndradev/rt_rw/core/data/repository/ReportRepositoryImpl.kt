package org.purboyndradev.rt_rw.core.data.repository

import org.purboyndradev.rt_rw.core.data.remote.api.ReportApi
import org.purboyndradev.rt_rw.core.data.remote.mapper.toReportModel
import org.purboyndradev.rt_rw.core.data.remote.params.CreateReportParams
import org.purboyndradev.rt_rw.core.data.remote.params.PaginationParams
import org.purboyndradev.rt_rw.core.data.remote.params.QueryParams
import org.purboyndradev.rt_rw.core.domain.model.ReportModel
import org.purboyndradev.rt_rw.core.network.DataNotFoundException
import org.purboyndradev.rt_rw.domain.repository.ReportRepository

class ReportRepositoryImpl(private val reportApi: ReportApi) : ReportRepository {
    override suspend fun createRepository(params: CreateReportParams) {
        reportApi.createReport(params)
    }

    override suspend fun fetchAllReports(
        paginationParams: PaginationParams?,
        queryParams: QueryParams?
    ): List<ReportModel> {
        val response = reportApi.fetchAllReports(paginationParams, queryParams)
        return response.data?.map {
            it.toReportModel()
        } ?: emptyList()
    }

    override suspend fun fetchReportById(id: String): ReportModel {
        val response = reportApi.fetchReportById(id)
        return response.data?.toReportModel() ?: throw DataNotFoundException("Report not found")
    }
}