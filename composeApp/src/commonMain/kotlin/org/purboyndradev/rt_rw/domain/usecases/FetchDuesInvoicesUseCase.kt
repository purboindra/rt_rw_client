package org.purboyndradev.rt_rw.domain.usecases

import org.purboyndradev.rt_rw.core.domain.AppError
import org.purboyndradev.rt_rw.core.domain.Result
import org.purboyndradev.rt_rw.core.domain.model.DuesInvoiceModel
import org.purboyndradev.rt_rw.domain.repository.DuesInvoiceRepository

class FetchDuesInvoicesUseCase(private val repository: DuesInvoiceRepository) {
    suspend operator fun invoke(): Result<List<DuesInvoiceModel>, AppError> {
        return when (val result = repository.fetchDuesInvoices()) {
            is Result.Success -> {
                Result.Success(result.data)
            }
            is Result.Error -> {
                Result.Error(result.error)
            }
        }
    }
}