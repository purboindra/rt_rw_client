package org.purboyndradev.rt_rw.domain.repository

import org.purboyndradev.rt_rw.core.data.remote.params.PaginationParams
import org.purboyndradev.rt_rw.core.data.remote.params.QueryParams
import org.purboyndradev.rt_rw.core.domain.AppError
import org.purboyndradev.rt_rw.core.domain.Result
import org.purboyndradev.rt_rw.core.domain.model.DuesInvoiceModel

interface DuesInvoiceRepository {
    suspend fun fetchAllDuesInvoices(
        paginationParams: PaginationParams? = null,
        queryParams: QueryParams? = null
    ): Result<List<DuesInvoiceModel>, AppError>

    suspend fun fetchDuesInvoiceById(id: String): Result<DuesInvoiceModel, AppError>
}