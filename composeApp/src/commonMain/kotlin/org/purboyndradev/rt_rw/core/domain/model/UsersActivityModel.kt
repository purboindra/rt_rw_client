package org.purboyndradev.rt_rw.core.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class UsersActivityModel(
    val id: String,
    val name: String,
    val phone: String,
    val email: String?,
    val role: String,
    val address: String,
    val rtId: String,
    val isVerified: Boolean,
    val emailVerified: Boolean,
    val image: String?,
)