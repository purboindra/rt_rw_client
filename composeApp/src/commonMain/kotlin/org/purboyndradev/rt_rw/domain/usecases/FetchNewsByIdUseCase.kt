package org.purboyndradev.rt_rw.domain.usecases

import org.purboyndradev.rt_rw.core.data.remote.mapper.toNewsModel
import org.purboyndradev.rt_rw.core.domain.AppError
import org.purboyndradev.rt_rw.core.domain.Result
import org.purboyndradev.rt_rw.core.domain.model.NewsModel
import org.purboyndradev.rt_rw.core.network.mapKtorExceptionToAppError
import org.purboyndradev.rt_rw.domain.repository.NewsRepository

class FetchNewsByIdUseCase(private val newsRepository: NewsRepository) {
    suspend operator fun invoke(id: String): Result<NewsModel, AppError> {
        return try {
            val result = newsRepository.fetchNewsById(id)
            val data = result.data
            if (data == null) return Result.Error(AppError.Remote.NotFound)
            Result.Success(data.toNewsModel())
        } catch (e: Exception) {
            Result.Error(mapKtorExceptionToAppError(e))
        }
    }
}