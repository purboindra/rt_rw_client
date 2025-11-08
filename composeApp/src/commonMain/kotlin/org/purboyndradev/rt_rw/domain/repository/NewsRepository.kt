package org.purboyndradev.rt_rw.domain.repository

import org.purboyndradev.rt_rw.core.data.remote.params.PaginationParams
import org.purboyndradev.rt_rw.core.data.remote.params.QueryParams
import org.purboyndradev.rt_rw.core.domain.model.NewsModel

interface NewsRepository {
    suspend fun fetchAllNews(
        paginationParams: PaginationParams? = null,
        queryParams: QueryParams? = null
    ): List<NewsModel>

    suspend fun fetchNewsById(id: String): NewsModel

}