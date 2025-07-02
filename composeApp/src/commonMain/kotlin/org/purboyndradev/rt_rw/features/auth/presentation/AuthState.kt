package org.purboyndradev.rt_rw.features.auth.presentation

import androidx.compose.runtime.Stable

@Stable
data class AuthState(
    val error: String? = null,
    val success: Boolean = false
)