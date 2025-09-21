package org.purboyndradev.rt_rw.core.data.repository

import org.purboyndradev.rt_rw.core.data.dto.BannerDto
import org.purboyndradev.rt_rw.core.data.dto.ResponseDto
import org.purboyndradev.rt_rw.core.data.remote.api.BannerApi
import org.purboyndradev.rt_rw.core.data.remote.params.PaginationParams
import org.purboyndradev.rt_rw.core.data.remote.params.QueryParams
import org.purboyndradev.rt_rw.core.domain.AppError
import org.purboyndradev.rt_rw.core.domain.Result
import org.purboyndradev.rt_rw.core.domain.mapBoth
import org.purboyndradev.rt_rw.domain.repository.BannerRepository

class BannerRepositoryImpl(private val bannerApi: BannerApi) : BannerRepository {
    override suspend fun fetchAllBanners(
        paginationParams: PaginationParams?,
        queryParams: QueryParams?
    ): Result<ResponseDto<List<BannerDto>>, AppError> {
        return bannerApi.fetchAllBanners(
            paginationParams = paginationParams,
            queryParams = queryParams,
        ).mapBoth(
            onSuccess = { data ->
                Result.Success(data)
            },
            onFailure = { error -> Result.Error(error) }
        )
    }
}