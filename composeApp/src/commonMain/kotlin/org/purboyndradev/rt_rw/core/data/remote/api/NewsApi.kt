package org.purboyndradev.rt_rw.core.data.remote.api

import org.purboyndradev.rt_rw.core.data.dto.NewsDto
import org.purboyndradev.rt_rw.core.data.dto.ResponseDto
import org.purboyndradev.rt_rw.core.data.remote.params.PaginationParams
import org.purboyndradev.rt_rw.core.data.remote.params.QueryParams

interface NewsApi {

    suspend fun fetchAllNews(
        paginationParams: PaginationParams? = null,
        queryParams: QueryParams? = null
    ): ResponseDto<List<NewsDto>>

    suspend fun fetchNewsById(id: String): ResponseDto<NewsDto>

}