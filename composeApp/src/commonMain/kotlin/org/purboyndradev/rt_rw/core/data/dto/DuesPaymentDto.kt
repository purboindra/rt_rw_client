package org.purboyndradev.rt_rw.core.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class DuesPaymentDto(
    val id: String,
    val invoiceNo: String,
    val paidAmount: Int? = null,
    val paidAt: String? = null,
    val method: String? = null,
    val proofUrl: String? = null,
    val note: String? = null,
    val status: String? = null,
    val createdAt: String? = null,
)