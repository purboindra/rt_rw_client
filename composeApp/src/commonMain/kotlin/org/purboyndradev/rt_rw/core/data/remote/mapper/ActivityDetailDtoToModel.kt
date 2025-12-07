package org.purboyndradev.rt_rw.core.data.remote.mapper

import org.purboyndradev.rt_rw.core.data.dto.ActivityDetailDto
import org.purboyndradev.rt_rw.core.domain.model.ActivityDetailModel
import org.purboyndradev.rt_rw.helper.ActivityType

fun ActivityDetailDto.toActivityDetailModel(): ActivityDetailModel {
    return ActivityDetailModel(
        id = id,
        title = title,
        description = description,
        rt = rt,
        date = date,
        pic = pic,
        bannerImageUrl = bannerImageUrl,
        imageUrl = imageUrl,
        type = runCatching {
            ActivityType.valueOf(type)
        }.getOrElse {
            println("Warning: Could not parse type '$type'. Defaulting to RONDA.")
            ActivityType.RONDA
        },
        createdBy = createdBy.toCreatedByModel(),
        users = users,
        activityId = activityId
    )
}