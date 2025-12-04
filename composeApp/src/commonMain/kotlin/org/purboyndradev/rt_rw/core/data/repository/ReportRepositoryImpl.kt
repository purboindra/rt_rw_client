package org.purboyndradev.rt_rw.core.data.repository

import org.purboyndradev.rt_rw.core.data.remote.api.ReportApi
import org.purboyndradev.rt_rw.core.data.remote.mapper.toReportModel
import org.purboyndradev.rt_rw.core.data.remote.params.CreateReportParams
import org.purboyndradev.rt_rw.core.data.remote.params.PaginationParams
import org.purboyndradev.rt_rw.core.data.remote.params.QueryParams
import org.purboyndradev.rt_rw.core.domain.AppError
import org.purboyndradev.rt_rw.core.domain.Result
import org.purboyndradev.rt_rw.core.domain.model.ReportModel
import org.purboyndradev.rt_rw.core.network.DataNotFoundException
import org.purboyndradev.rt_rw.core.network.mapKtorExceptionToAppError
import org.purboyndradev.rt_rw.domain.repository.ReportRepository

class ReportRepositoryImpl(private val reportApi: ReportApi) : ReportRepository {
    override suspend fun createRepository(params: CreateReportParams) {
        reportApi.createReport(params)
    }

    override suspend fun fetchAllReports(
        paginationParams: PaginationParams?,
        queryParams: QueryParams?
    ): Result<List<ReportModel>, AppError> {
        return try {
            val response = reportApi.fetchAllReports(paginationParams, queryParams)
            if (response.data != null) {
                Result.Success(response.data.map { it.toReportModel() })
            } else {
                throw DataNotFoundException("Tidak ada data laporan")
            }
        } catch (e: Exception) {
            Result.Error(mapKtorExceptionToAppError(e))
        }
    }

    override suspend fun fetchReportById(id: String): Result<ReportModel?, AppError> {

        return try {
            val response = reportApi.fetchReportById(id)
            if (response.data != null) {
                Result.Success(response.data.toReportModel())
            } else {
                throw DataNotFoundException("Laporan tidak ditemukan")
            }
        } catch (e: Exception) {
            Result.Error(mapKtorExceptionToAppError(e))
        }
    }
}