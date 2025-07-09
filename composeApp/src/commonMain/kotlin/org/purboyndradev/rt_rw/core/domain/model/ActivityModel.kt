package org.purboyndradev.rt_rw.core.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ActivityModel(
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
)