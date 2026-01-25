package org.purboyndradev.rt_rw.core.data.remote.mapper

import org.purboyndradev.rt_rw.core.data.dto.UserDto
import org.purboyndradev.rt_rw.core.domain.model.UserModel

fun UserDto.toUserModel(): UserModel {
    return UserModel(
        id = id,
        name = name,
        phoneNumber = phone,
        email = email,
        role = role,
        rt = rt?.toRTModel()
    )
}