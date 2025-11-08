package org.purboyndradev.rt_rw.domain.usecases

import org.purboyndradev.rt_rw.core.domain.AppError
import org.purboyndradev.rt_rw.core.domain.Result
import org.purboyndradev.rt_rw.core.domain.model.NewsModel
import org.purboyndradev.rt_rw.core.network.mapKtorExceptionToAppError
import org.purboyndradev.rt_rw.domain.repository.NewsRepository

class FetchNewsByIdUseCase(private val newsRepository: NewsRepository) {
    suspend operator fun invoke(id: String): Result<NewsModel, AppError> {
        return try {
            val news = newsRepository.fetchNewsById(id)
            Result.Success(news)
        } catch (e: Exception) {
            Result.Error(mapKtorExceptionToAppError(e))
        }
    }
}