package org.purboyndradev.rt_rw.features.profile.presentation

import org.purboyndradev.rt_rw.core.domain.model.UserModel

data class CurrentUserState(
    val loading: Boolean = false,
    val error: String? = null,
    val data: UserModel? = null,
    val success: Boolean = false
)
