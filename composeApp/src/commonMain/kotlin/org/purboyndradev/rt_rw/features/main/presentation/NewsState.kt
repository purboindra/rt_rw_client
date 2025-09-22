package org.purboyndradev.rt_rw.features.main.presentation

import androidx.compose.runtime.Stable
import org.purboyndradev.rt_rw.core.domain.model.NewsModel


@Stable
data class NewsState(
    val news: List<NewsModel> = emptyList(),
    val loading: Boolean = false,
    val error: String? = null
)