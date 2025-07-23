package org.purboyndradev.rt_rw.core.data.remote.mapper

import org.purboyndradev.rt_rw.core.data.dto.RTDto
import org.purboyndradev.rt_rw.core.domain.model.RTModel

fun RTDto.toRTModel(): RTModel {
    return RTModel(
        id = id,
        name = name,
        address = address
    )
}