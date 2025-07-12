package org.purboyndradev.rt_rw.core.data.remote.mapper

import org.purboyndradev.rt_rw.core.data.dto.ActivityDto
import org.purboyndradev.rt_rw.core.domain.model.ActivityModel
import org.purboyndradev.rt_rw.domain.model.UserDBModel

fun ActivityDto.toActivityModel(): ActivityModel {
    return ActivityModel(
        id = id,
        title = title,
        description = description,
        rt = "",
        date = date,
        pic = UserDBModel(
            accessToken = "",
            refreshToken = "",
            username = "",
            email = "",
        ),
        type = ""
    )
}