package org.purboyndradev.rt_rw.core.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class RTDto(
    val id: String,
    val name: String,
    val address: String,
    val totalFunds: Int,
)