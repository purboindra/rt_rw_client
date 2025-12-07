package org.purboyndradev.rt_rw.core.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.purboyndradev.rt_rw.core.domain.model.UserModel

@Serializable
enum class ReportStatus {
    @SerialName("OPEN")
    OPEN,

    @SerialName("IN_PROGRESS")
    IN_PROGRESS,

    @SerialName("CLOSED")
    CLOSED,

    @SerialName("RESOLVED")
    RESOLVED
}

@Serializable
data class ReportDto(
    val id: String,
    val title: String,
    val createdAt: String,
    val rtId: String,
    val imageUrl: String,
    val description: String,
    val status: ReportStatus,
    val user: UserModel,
    val reportId: String,
    val resolvedAt: String? = null,
    val resolvedById: String? = null,

)