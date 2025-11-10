package org.purboyndradev.rt_rw.features.news.presentation

import androidx.compose.runtime.Stable
import org.purboyndradev.rt_rw.core.domain.model.NewsModel

@Stable
data class NewsState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val news: List<NewsModel> = emptyList(),
    val detailNews: NewsModel? = null
)