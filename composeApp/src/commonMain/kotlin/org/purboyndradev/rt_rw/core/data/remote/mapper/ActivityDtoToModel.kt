package org.purboyndradev.rt_rw.core.data.remote.mapper

import org.purboyndradev.rt_rw.core.data.dto.ActivityDto
import org.purboyndradev.rt_rw.core.domain.model.ActivityModel
import org.purboyndradev.rt_rw.helper.ActivityType

fun ActivityDto.toActivityModel(): ActivityModel {
    return ActivityModel(
        id = id,
        title = title,
        description = description,
        rtId = rtId,
        date = date,
        type = runCatching {
            ActivityType.valueOf(type)
        }.getOrElse {
            ActivityType.RONDA
        },
        pic = pic,
        bannerImageUrl = bannerImageUrl,
        imageUrl = imageUrl,
        activityId=activityId,

    )
}