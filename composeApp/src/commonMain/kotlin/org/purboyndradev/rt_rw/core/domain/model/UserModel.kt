package org.purboyndradev.rt_rw.core.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserModel(
    val id: String,
    val name: String,
    @SerialName("phone")
    val phoneNumber: String = "",
    val email: String? = null,
    val address: String = ""
)