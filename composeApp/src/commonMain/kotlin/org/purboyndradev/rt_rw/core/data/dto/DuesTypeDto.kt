package org.purboyndradev.rt_rw.core.data.dto

import kotlinx.serialization.Serializable

@Serializable

data class DuesTypeDto(
    val id: String = "",
    val status: String,
    val defaultAmount: Int,
    val name: String,
    val code: String,
    val frequency: String = "",
    val isActive: Boolean = true,
    val createdAt: String = "",
)