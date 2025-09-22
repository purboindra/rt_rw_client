package org.purboyndradev.rt_rw.core.data.dto

import kotlinx.serialization.Serializable
import org.purboyndradev.rt_rw.core.domain.model.RTModel
import org.purboyndradev.rt_rw.core.domain.model.UserModel

@Serializable
data class NewsDto(
    val id: String,
    val title: String,
    val description: String,
    val body: String,
    val rt: RTModel,
    val user: UserModel
)