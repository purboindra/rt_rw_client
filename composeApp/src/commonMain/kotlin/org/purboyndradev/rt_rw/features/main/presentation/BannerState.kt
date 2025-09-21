package org.purboyndradev.rt_rw.features.main.presentation

import androidx.compose.runtime.Stable
import org.purboyndradev.rt_rw.core.domain.model.BannerModel


@Stable
data class BannerState(
    val banners: List<BannerModel> = emptyList<BannerModel>(),
    val error: String? = null,
    val loading: Boolean = false
)