package org.purboyndradev.rt_rw.core.data.dto

import kotlinx.serialization.Serializable

@Serializable

data class UserDto(
    val id: String,
    val name: String,
    val phone: String,
    val email: String,
    val role: String,
    val image: String? = null,
    val rt: RTDto? = null,
)