package org.purboyndradev.rt_rw.core.data.dto

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class SignInDto(
    /// This field for handle if got code USER_NOT_VERIFIED
    @SerialName("redirect_url")
    val redirectUrl: String? = null,
    @SerialName("access_token")
    val accessToken: String = "",
    @SerialName("refresh_token")
    val refreshToken: String = "",
)