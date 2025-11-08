package org.purboyndradev.rt_rw.core.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.purboyndradev.rt_rw.core.data.dto.ReportStatus

@Serializable
data class ReportModel(
    @SerialName("id")
    val id:String,
    @SerialName("title")
    val title:String,
    @SerialName("description")
    val description:String,
    @SerialName("createdAt")
    val createdAt:String,
    @SerialName("rtId")
    val rtId:String,
    @SerialName("imageUrl")
    val imageUrl:String,
    @SerialName("status")
    val status: ReportStatus,
    @SerialName("user")
    val user:UserModel
)