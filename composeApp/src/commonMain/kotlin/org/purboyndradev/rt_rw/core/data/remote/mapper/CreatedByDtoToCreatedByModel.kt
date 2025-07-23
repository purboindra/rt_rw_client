package org.purboyndradev.rt_rw.core.data.remote.mapper

import org.purboyndradev.rt_rw.core.data.dto.CreatedByDto
import org.purboyndradev.rt_rw.core.domain.model.CreatedByModel

fun CreatedByDto.toCreatedByModel(): CreatedByModel {
    
    return CreatedByModel(
        id = id,
        name = name,
        rt = rt.toRTModel(),
        role = role,
    )
}