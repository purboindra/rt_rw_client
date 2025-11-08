package org.purboyndradev.rt_rw.core.data.remote.mapper

import org.purboyndradev.rt_rw.core.data.dto.ReportDto
import org.purboyndradev.rt_rw.core.domain.model.ReportModel

fun ReportDto.toReportModel(): ReportModel {
    return ReportModel(
        id = id,
        title = title,
        description = description,
        createdAt = createdAt,
        rtId = rtId,
        imageUrl = imageUrl,
        status = status,
        user = user
    )
}