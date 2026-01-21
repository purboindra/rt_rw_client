package org.purboyndradev.rt_rw.core.data.remote.mapper

import org.purboyndradev.rt_rw.core.data.dto.DuesTypeDto
import org.purboyndradev.rt_rw.core.domain.model.DuesTypeModel

fun DuesTypeDto.toDuesTypeModel(): DuesTypeModel {
    return DuesTypeModel(
        id = id,
        status = status,
        defaultAmount = defaultAmount,
        name = name,
        code = code,
    )
}