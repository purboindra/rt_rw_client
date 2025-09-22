package org.purboyndradev.rt_rw.domain.repository

import org.purboyndradev.rt_rw.core.data.dto.NewsDto
import org.purboyndradev.rt_rw.core.data.dto.ResponseDto
import org.purboyndradev.rt_rw.core.data.remote.params.PaginationParams
import org.purboyndradev.rt_rw.core.data.remote.params.QueryParams
import org.purboyndradev.rt_rw.core.domain.AppError
import org.purboyndradev.rt_rw.core.domain.Result

interface NewsRepository {
    suspend fun fetchAllNews(
        paginationParams: PaginationParams? = null,
        queryParams: QueryParams? = null
    ): Result<ResponseDto<List<NewsDto>>, AppError>

    suspend fun fetchNewsById(id: String): Result<ResponseDto<NewsDto>, AppError>

}