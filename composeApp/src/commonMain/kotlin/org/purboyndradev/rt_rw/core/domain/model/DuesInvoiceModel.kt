package org.purboyndradev.rt_rw.core.domain.model

import kotlinx.serialization.Serializable
import org.purboyndradev.rt_rw.helper.DuesInvoiceStatus

@Serializable
data class DuesInvoiceModel(
    val id: String,
    val invoiceNo: String,
    val status: DuesInvoiceStatus,
    val duesType: DuesTypeModel,
    val period: Int? = null,
    val amount: Int? = null,
    val dueDate: String = "",
    val createdAt: String = "",
    val createdBy: String = "",
    val payments: List<DuesPaymentModel> = emptyList()
)