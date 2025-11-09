package org.purboyndradev.rt_rw.core.data.repository

import org.purboyndradev.rt_rw.core.data.remote.api.NewsApi
import org.purboyndradev.rt_rw.core.data.remote.mapper.toNewsModel
import org.purboyndradev.rt_rw.core.data.remote.params.PaginationParams
import org.purboyndradev.rt_rw.core.data.remote.params.QueryParams
import org.purboyndradev.rt_rw.core.domain.model.NewsModel
import org.purboyndradev.rt_rw.core.network.DataNotFoundException
import org.purboyndradev.rt_rw.domain.repository.NewsRepository

class NewsRepositoryImpl(private val newsApi: NewsApi) : NewsRepository {
    override suspend fun fetchAllNews(
        paginationParams: PaginationParams?,
        queryParams: QueryParams?
    ): List<NewsModel> {
        val result = newsApi.fetchAllNews(paginationParams, queryParams)
        val allNews = result.data?.map {
            it.toNewsModel()
        } ?: emptyList()
        return allNews
    }

    override suspend fun fetchNewsById(id: String): NewsModel {
        val result = newsApi.fetchNewsById(id)
        val news = result.data?.toNewsModel()
        return news ?: throw DataNotFoundException("News not found")
    }
}