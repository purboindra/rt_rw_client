package org.purboyndradev.rt_rw.core.data.dto

import kotlinx.serialization.Serializable

@Serializable

data class DuesInvoiceDto(
    val id: String,
    val invoiceNo: String,
    val duesType: DuesTypeDto,
    val rtId: String = "",
    val householdId: String = "",
    val duesTypeId: String = "",
    val period: Int? = null,
    val amount: Int? = null,
    val dueDate: String = "",
    val status: String = "",
    val createdAt: String = "",
    val createdBy: String = "",
    val payments: List<DuesInvoiceDto> = emptyList()
)