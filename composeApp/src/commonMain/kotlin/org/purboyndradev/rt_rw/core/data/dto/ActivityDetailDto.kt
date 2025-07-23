package org.purboyndradev.rt_rw.core.data.dto


import kotlinx.serialization.Serializable
import org.purboyndradev.rt_rw.core.domain.model.UserModel

@Serializable
data class ActivityDetailDto(
    val id: String,
    val title: String,
    val date: Int,
    val type: String,
    val picId: String,
    val description: String,
    val rtId: String,
    val bannerImageUrl: String? = null,
    val imageUrl: String? = null,
    val createdBy: CreatedByDto,
    val pic: UserModel
)