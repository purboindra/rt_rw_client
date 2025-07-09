package org.purboyndradev.rt_rw.core.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class ActivityDto(
    val id: String,
    val title: String,
    val date: Int,
    val type: String,
    val picId: String,
    val description: String,
)