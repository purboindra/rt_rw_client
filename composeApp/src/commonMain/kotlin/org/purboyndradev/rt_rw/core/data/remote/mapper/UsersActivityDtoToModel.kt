package org.purboyndradev.rt_rw.core.data.remote.mapper

import org.purboyndradev.rt_rw.core.data.dto.UsersActivityDto
import org.purboyndradev.rt_rw.core.domain.model.UsersActivityModel

fun UsersActivityDto.toUsersActivityModel(): UsersActivityModel {
    return UsersActivityModel(
        id = id,
        name = name,
        phone = phone,
        email = email,
        role = role,
        address = address,
        rtId = rtId,
        isVerified = isVerified,
        emailVerified = emailVerified,
        image = image,
    )
}