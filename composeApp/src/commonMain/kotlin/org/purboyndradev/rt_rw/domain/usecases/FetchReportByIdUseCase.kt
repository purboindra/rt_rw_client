package org.purboyndradev.rt_rw.domain.usecases

import org.purboyndradev.rt_rw.core.domain.AppError
import org.purboyndradev.rt_rw.core.domain.Result
import org.purboyndradev.rt_rw.core.domain.model.ReportModel
import org.purboyndradev.rt_rw.core.network.DataNotFoundException
import org.purboyndradev.rt_rw.domain.repository.ReportRepository

class FetchReportByIdUseCase(private val reportRepository: ReportRepository) {
    suspend operator fun invoke(id: String): Result<ReportModel, AppError> {
        return when (val result = reportRepository.fetchReportById(id)) {
            is Result.Success -> {
                val report = result.data ?: throw DataNotFoundException("Report tidak ditemukan")
                Result.Success(report)
            }

            is Result.Error -> {
                Result.Error(result.error)
            }
        }
    }
}