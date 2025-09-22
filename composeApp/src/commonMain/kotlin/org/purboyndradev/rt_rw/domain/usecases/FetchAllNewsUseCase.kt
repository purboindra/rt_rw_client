package org.purboyndradev.rt_rw.domain.usecases

import org.purboyndradev.rt_rw.core.data.remote.mapper.toNewsModel
import org.purboyndradev.rt_rw.core.data.remote.params.PaginationParams
import org.purboyndradev.rt_rw.core.data.remote.params.QueryParams
import org.purboyndradev.rt_rw.core.domain.AppError
import org.purboyndradev.rt_rw.core.domain.Result
import org.purboyndradev.rt_rw.core.domain.model.NewsModel
import org.purboyndradev.rt_rw.domain.repository.NewsRepository

class FetchAllNewsUseCase(private val newsRepository: NewsRepository) {
    suspend operator fun invoke(
        paginationParams: PaginationParams? = null,
        queryParams: QueryParams? = null
    ): Result<List<NewsModel>, AppError> {
        return when (val result =
            newsRepository.fetchAllNews(paginationParams, queryParams)) {
            is Result.Success -> {
                val data = result.data.data;
                if (data == null) return Result.Error(AppError.Remote.NotFound)
                val news = data.map {
                    it.toNewsModel()
                }
                Result.Success(news)
            }

            is Result.Error -> {
                val error = result.error
                Result.Error(error)
            }
        }
    }
}