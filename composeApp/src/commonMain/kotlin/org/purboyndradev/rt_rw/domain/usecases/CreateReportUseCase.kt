package org.purboyndradev.rt_rw.domain.usecases

import org.purboyndradev.rt_rw.core.data.remote.params.CreateReportParams
import org.purboyndradev.rt_rw.core.domain.AppError
import org.purboyndradev.rt_rw.core.domain.Result
import org.purboyndradev.rt_rw.core.network.mapKtorExceptionToAppError
import org.purboyndradev.rt_rw.domain.repository.ReportRepository

class CreateReportUseCase(private val reportRepository: ReportRepository) {
    suspend operator fun invoke(params: CreateReportParams): Result<Unit, AppError> {
        return try {
            reportRepository.createRepository(params)
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(mapKtorExceptionToAppError(e))
        }
    }
}