package org.purboyndradev.rt_rw.features.activity.presentation

import androidx.compose.runtime.Stable
import org.purboyndradev.rt_rw.core.domain.model.ActivityDetailModel
import org.purboyndradev.rt_rw.core.domain.model.ActivityModel

@Stable
data class ActivityState(
    val activities: List<ActivityModel>,
    val activity: ActivityDetailModel? = null,
    val error: String? = null,
    val loading: Boolean = false
)