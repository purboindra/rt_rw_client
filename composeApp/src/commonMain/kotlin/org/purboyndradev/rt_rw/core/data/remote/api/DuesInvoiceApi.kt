package org.purboyndradev.rt_rw.core.data.remote.api

import org.purboyndradev.rt_rw.core.data.dto.DuesInvoiceDto
import org.purboyndradev.rt_rw.core.data.dto.ResponseDto
import org.purboyndradev.rt_rw.core.data.remote.params.PaginationParams
import org.purboyndradev.rt_rw.core.data.remote.params.QueryParams

interface DuesInvoiceApi {

    suspend fun fetchDuesInvoices(
        paginationParams: PaginationParams? = null,
        queryParams: QueryParams? = null
    ): ResponseDto<List<DuesInvoiceDto>>

    suspend fun fetchDuesInvoiceById(
        id: String
    ): ResponseDto<DuesInvoiceDto>
}