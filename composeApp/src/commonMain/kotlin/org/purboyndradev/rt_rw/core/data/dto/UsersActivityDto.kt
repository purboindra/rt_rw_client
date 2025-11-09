package org.purboyndradev.rt_rw.core.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class UsersActivityDto(
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

@Serializable
data class UsersActivityWrapperDto(
    val users: List<UsersActivityDto>
)