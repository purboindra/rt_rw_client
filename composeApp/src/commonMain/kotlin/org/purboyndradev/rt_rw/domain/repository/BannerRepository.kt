package org.purboyndradev.rt_rw.domain.repository

import org.purboyndradev.rt_rw.core.data.dto.ActivityDto
import org.purboyndradev.rt_rw.core.data.dto.BannerDto
import org.purboyndradev.rt_rw.core.data.dto.ResponseDto
import org.purboyndradev.rt_rw.core.data.remote.params.PaginationParams
import org.purboyndradev.rt_rw.core.data.remote.params.QueryParams
import org.purboyndradev.rt_rw.core.domain.AppError
import org.purboyndradev.rt_rw.core.domain.Result

interface BannerRepository {
    suspend fun fetchAllBanners(
        paginationParams: PaginationParams? = null,
        queryParams: QueryParams? = null
    ): Result<ResponseDto<List<BannerDto>>, AppError>
}