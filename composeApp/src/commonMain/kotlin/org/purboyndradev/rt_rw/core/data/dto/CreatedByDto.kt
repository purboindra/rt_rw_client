package org.purboyndradev.rt_rw.core.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class CreatedByDto(
    val id: String,
    val name: String,
    val role: String,
    val rt: RTDto
)