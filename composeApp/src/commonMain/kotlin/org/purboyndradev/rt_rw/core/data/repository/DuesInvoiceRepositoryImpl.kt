package org.purboyndradev.rt_rw.core.data.repository

import org.purboyndradev.rt_rw.core.data.remote.api.DuesInvoiceApi
import org.purboyndradev.rt_rw.core.data.remote.mapper.toDuesInvoiceModel
import org.purboyndradev.rt_rw.core.data.remote.params.PaginationParams
import org.purboyndradev.rt_rw.core.data.remote.params.QueryParams
import org.purboyndradev.rt_rw.core.domain.AppError
import org.purboyndradev.rt_rw.core.domain.Result
import org.purboyndradev.rt_rw.core.domain.model.DuesInvoiceModel
import org.purboyndradev.rt_rw.core.network.DataNotFoundException
import org.purboyndradev.rt_rw.core.network.mapKtorExceptionToAppError
import org.purboyndradev.rt_rw.domain.repository.DuesInvoiceRepository

class DuesInvoiceRepositoryImpl(private val api: DuesInvoiceApi) : DuesInvoiceRepository {
    override suspend fun fetchAllDuesInvoices(
        paginationParams: PaginationParams?,
        queryParams: QueryParams?,
    ): Result<List<DuesInvoiceModel>, AppError> {
        return try {
            val result = api.fetchAllDuesInvoices(paginationParams, queryParams)
            val duesInvoices = result.data?.map {
                it.toDuesInvoiceModel()
            } ?: emptyList()
            Result.Success(duesInvoices)
        } catch (e: Exception) {
            Result.Error(mapKtorExceptionToAppError(e))
        }
    }

    override suspend fun fetchDuesInvoiceById(id: String): Result<DuesInvoiceModel, AppError> {
        return try {
            val result = api.fetchDuesInvoiceById(id)
            val duesInvoice = result.data ?: throw DataNotFoundException("Invoice tidak ketemu")
            Result.Success(duesInvoice.toDuesInvoiceModel())
        } catch (e: Exception) {
            Result.Error(mapKtorExceptionToAppError(e))
        }
    }
}