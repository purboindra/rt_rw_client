package org.purboyndradev.rt_rw.core.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class ActivityDetailModel(
    @SerialName("id")
    val id: String,
    @SerialName("title")
    val title: String,
    @SerialName("description")
    val description: String,
    // TODO: add RT type
    @SerialName("rt")
    val rt: String? = null,
    @SerialName("date")
    val date: Int,
    @SerialName("pic")
    val pic: UserModel,
    @SerialName("users")
    val users: List<UserModel> = emptyList<UserModel>(),
    @SerialName("bannerImageUrl")
    val bannerImageUrl: String? = null,
    @SerialName("imageUrl")
    val imageUrl: String? = null,
    @SerialName("type")
    val type: String,
    @SerialName("createdBy")
    val createdBy: CreatedByModel
)
