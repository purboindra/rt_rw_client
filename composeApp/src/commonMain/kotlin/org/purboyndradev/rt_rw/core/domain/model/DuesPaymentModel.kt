package org.purboyndradev.rt_rw.core.domain.model

import kotlinx.serialization.Serializable
import org.purboyndradev.rt_rw.helper.DuesInvoiceStatus
import org.purboyndradev.rt_rw.helper.PaymentMethod

@Serializable

data class DuesPaymentModel(
    val id: String,
    val invoiceId: String,
    val paidAmount: Int? = null,
    val method: PaymentMethod,
    val proofUrl: String? = null,
    val note: String? = null,
    val status: DuesInvoiceStatus,
    val createdAt: String = "",
    val createdBy: String = "",
)