package org.purboyndradev.rt_rw.domain.usecases

import org.purboyndradev.rt_rw.core.data.remote.mapper.toNewsModel
import org.purboyndradev.rt_rw.core.data.remote.params.PaginationParams
import org.purboyndradev.rt_rw.core.data.remote.params.QueryParams
import org.purboyndradev.rt_rw.core.domain.AppError
import org.purboyndradev.rt_rw.core.domain.Result
import org.purboyndradev.rt_rw.core.domain.model.NewsModel
import org.purboyndradev.rt_rw.core.network.mapKtorExceptionToAppError
import org.purboyndradev.rt_rw.domain.repository.NewsRepository

class FetchAllNewsUseCase(private val newsRepository: NewsRepository) {
    suspend operator fun invoke(
        paginationParams: PaginationParams? = null,
        queryParams: QueryParams? = null
    ): Result<List<NewsModel>, AppError> {
        return try {
            val result = newsRepository.fetchAllNews(paginationParams, queryParams)
            val data = result.data
            if (data == null) return Result.Error(AppError.Remote.NotFound)
            var news: List<NewsModel> = emptyList()
            data.forEach {
                news = news + it.toNewsModel()
            }
            Result.Success(news)
        } catch (e: Exception) {
            Result.Error(mapKtorExceptionToAppError(e))
        }
    }
}