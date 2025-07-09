package org.purboyndradev.rt_rw.core.data.remote.params

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CreateActivityParams(
    @SerialName("title")
    val title: String,
    @SerialName("date")
    val date: Int,
    @SerialName("type")
    val type: String,
    @SerialName("pic_id")
    val picId: String
)
