package org.purboyndradev.rt_rw.core.data.remote.mapper

import org.purboyndradev.rt_rw.core.data.dto.DuesInvoiceDto
import org.purboyndradev.rt_rw.core.domain.model.DuesInvoiceModel
import org.purboyndradev.rt_rw.helper.DuesInvoiceStatus

fun DuesInvoiceDto.toDuesInvoiceModel(): DuesInvoiceModel {
    return DuesInvoiceModel(
        id = id,
        invoiceNo = invoiceNo,
        status = DuesInvoiceStatus.fromString(status),
        duesType = duesType.toDuesTypeModel(),
        period = period,
        amount = amount,
        dueDate = dueDate,
        createdAt = createdAt,
        createdBy = createdBy,
    )
}