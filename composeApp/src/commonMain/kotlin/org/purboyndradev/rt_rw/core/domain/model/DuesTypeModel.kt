package org.purboyndradev.rt_rw.core.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable

data class DuesTypeModel(
    @SerialName("id")
    val id: String = "",
    @SerialName("status")
    val status: String,
    @SerialName("defaultAmount")
    val defaultAmount: Int,
    @SerialName("name")
    val name: String,
    @SerialName("code")
    val code: String,
    @SerialName("frequency")
    val frequency: String = "",
    @SerialName("isActive")
    val isActive: Boolean = true,
    @SerialName("createdAt")
    val createdAt: String = "",
)