package org.purboyndradev.rt_rw.features.profile.presentation

data class LogoutState(
    val loading: Boolean = false,
    val error: String? = null,
    val success: Boolean = false,
)
