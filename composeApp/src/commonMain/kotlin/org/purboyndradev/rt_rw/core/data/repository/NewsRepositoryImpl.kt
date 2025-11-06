package org.purboyndradev.rt_rw.core.data.repository

import org.purboyndradev.rt_rw.core.data.dto.NewsDto
import org.purboyndradev.rt_rw.core.data.dto.ResponseDto
import org.purboyndradev.rt_rw.core.data.remote.api.NewsApi
import org.purboyndradev.rt_rw.core.data.remote.params.PaginationParams
import org.purboyndradev.rt_rw.core.data.remote.params.QueryParams
import org.purboyndradev.rt_rw.domain.repository.NewsRepository

class NewsRepositoryImpl(private val newsApi: NewsApi) : NewsRepository {
    override suspend fun fetchAllNews(
        paginationParams: PaginationParams?,
        queryParams: QueryParams?
    ): ResponseDto<List<NewsDto>> {
        return newsApi.fetchAllNews(paginationParams, queryParams)
    }

    override suspend fun fetchNewsById(id: String): ResponseDto<NewsDto> {
        return newsApi.fetchNewsById(id)
    }
}