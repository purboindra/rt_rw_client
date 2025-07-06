package org.purboyndradev.rt_rw.core.domain.model

data class AuthTokenInfo(
    val accessToken: String,
    val refreshToken: String,
    val username: String,
    val email: String?,
    val redirectUrl: String?,
    val code: String?
)
