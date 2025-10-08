package org.purboyndradev.rt_rw.features.activity.presentation

import androidx.compose.runtime.Stable
import org.purboyndradev.rt_rw.core.domain.model.UsersActivityModel

@Stable
data class UsersActivityState(
    val isLoading: Boolean = false,
    val users: List<UsersActivityModel>,
    val error: String? = null
)
