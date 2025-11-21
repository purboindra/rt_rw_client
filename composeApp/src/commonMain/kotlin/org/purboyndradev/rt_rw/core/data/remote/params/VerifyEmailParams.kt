package org.purboyndradev.rt_rw.core.data.remote.params

import kotlinx.serialization.Serializable

@Serializable
data class VerifyEmailParams(
    val email: String,
    val code: String
)
