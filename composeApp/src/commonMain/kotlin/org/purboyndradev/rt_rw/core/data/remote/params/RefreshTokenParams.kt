package org.purboyndradev.rt_rw.core.data.remote.params

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RefreshTokenParams(
    @SerialName("refresh_token")
    val refreshToken: String
)

