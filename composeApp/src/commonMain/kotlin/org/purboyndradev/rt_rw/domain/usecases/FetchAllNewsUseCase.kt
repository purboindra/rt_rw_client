package org.purboyndradev.rt_rw.domain.usecases

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
            val allNews = newsRepository.fetchAllNews(paginationParams, queryParams)
            Result.Success(allNews)
        } catch (e: Exception) {
            Result.Error(mapKtorExceptionToAppError(e))
        }
    }
}