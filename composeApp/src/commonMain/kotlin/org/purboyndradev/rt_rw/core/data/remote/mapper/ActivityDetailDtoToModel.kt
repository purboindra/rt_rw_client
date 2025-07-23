package org.purboyndradev.rt_rw.core.data.remote.mapper

import org.purboyndradev.rt_rw.core.data.dto.ActivityDetailDto
import org.purboyndradev.rt_rw.core.domain.model.ActivityDetailModel

fun ActivityDetailDto.toActivityDetailModel(): ActivityDetailModel {
    return ActivityDetailModel(
        id = id,
        title = title,
        description = description,
        rt = rtId,
        date = date,
        pic = pic,
        bannerImageUrl = bannerImageUrl,
        imageUrl = imageUrl,
        type = type,
        createdBy = createdBy.toCreatedByModel()
    )
}