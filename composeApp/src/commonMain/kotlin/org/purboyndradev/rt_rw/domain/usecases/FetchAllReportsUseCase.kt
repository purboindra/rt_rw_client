package org.purboyndradev.rt_rw.domain.usecases

import org.purboyndradev.rt_rw.core.data.remote.params.PaginationParams
import org.purboyndradev.rt_rw.core.data.remote.params.QueryParams
import org.purboyndradev.rt_rw.core.domain.AppError
import org.purboyndradev.rt_rw.core.domain.Result
import org.purboyndradev.rt_rw.core.domain.model.ReportModel
import org.purboyndradev.rt_rw.domain.repository.ReportRepository

class FetchAllReportsUseCase(private val reportRepository: ReportRepository) {
    suspend operator fun invoke(
        paginationParams: PaginationParams? = null,
        queryParams: QueryParams? = null
    ): Result<List<ReportModel>, AppError> {
        return when (val result = reportRepository.fetchAllReports(paginationParams, queryParams)) {
            is Result.Success -> {
                Result.Success(result.data)
            }

            is Result.Error -> {
                Result.Error(result.error)
            }
        }
    }
}