package org.purboyndradev.rt_rw.core.data.remote.params

import kotlinx.serialization.Serializable


@Serializable
data class SignInParams(
    val phone: String
)