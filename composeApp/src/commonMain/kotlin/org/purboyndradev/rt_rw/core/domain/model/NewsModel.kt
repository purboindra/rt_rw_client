package org.purboyndradev.rt_rw.core.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class NewsModel(
    val id: String,
    val title: String,
    val description: String,
    val body: String,
    val rt: RTModel,
    val author: UserModel
)