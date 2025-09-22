package org.purboyndradev.rt_rw.core.data.repository

import org.purboyndradev.rt_rw.core.data.dto.NewsDto
import org.purboyndradev.rt_rw.core.data.dto.ResponseDto
import org.purboyndradev.rt_rw.core.data.remote.api.NewsApi
import org.purboyndradev.rt_rw.core.data.remote.params.PaginationParams
import org.purboyndradev.rt_rw.core.data.remote.params.QueryParams
import org.purboyndradev.rt_rw.core.domain.AppError
import org.purboyndradev.rt_rw.core.domain.Result
import org.purboyndradev.rt_rw.core.domain.mapBoth
import org.purboyndradev.rt_rw.domain.repository.NewsRepository

class NewsRepositoryImpl(private val newsApi: NewsApi) : NewsRepository {
    override suspend fun fetchAllNews(
        paginationParams: PaginationParams?,
        queryParams: QueryParams?
    ): Result<ResponseDto<List<NewsDto>>, AppError> {
        return newsApi.fetchAllNews(paginationParams, queryParams).mapBoth(
            onSuccess = {
                Result.Success(it)
            },
            onFailure = {
                Result.Error(it)
            }
        )
    }

    override suspend fun fetchNewsById(id: String): Result<ResponseDto<NewsDto>, AppError> {
        return newsApi.fetchNewsById(id).mapBoth(
            onSuccess = {
                Result.Success(it)
            },
            onFailure = {
                Result.Error(it)
            }
        )
    }
}