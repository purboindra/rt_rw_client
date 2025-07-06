package org.purboyndradev.rt_rw.core.domain.model


data class RefreshTokenInfo(
    val accessToken: String,
    val refreshToken: String,
)