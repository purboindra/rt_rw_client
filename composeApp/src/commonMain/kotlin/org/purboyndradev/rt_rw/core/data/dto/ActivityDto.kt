package org.purboyndradev.rt_rw.core.data.dto

import kotlinx.serialization.Serializable
import org.purboyndradev.rt_rw.core.domain.model.UserModel

@Serializable
data class ActivityDto(
    val id: String,
    val title: String,
    val date: Int,
    val type: String,
    val rtId: String,
    val createdById: String,
    val picId: String,
    val bannerImageUrl: String? = null,
    val imageUrl: String? = null,
    val description: String,
    val pic: UserModel,
    val activityId: String? = null
)