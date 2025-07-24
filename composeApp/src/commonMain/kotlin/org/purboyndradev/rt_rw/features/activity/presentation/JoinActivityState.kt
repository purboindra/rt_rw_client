package org.purboyndradev.rt_rw.features.activity.presentation

import androidx.compose.runtime.Stable

@Stable
data class JoinActivityState(
    val loading: Boolean = false,
    val error: String? = null,
    val success: Boolean = false
)
