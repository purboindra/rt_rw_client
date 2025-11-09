package org.purboyndradev.rt_rw.core.data.repository

import org.purboyndradev.rt_rw.core.data.remote.api.BannerApi
import org.purboyndradev.rt_rw.core.data.remote.mapper.toBannerModel
import org.purboyndradev.rt_rw.core.data.remote.params.PaginationParams
import org.purboyndradev.rt_rw.core.data.remote.params.QueryParams
import org.purboyndradev.rt_rw.core.domain.model.BannerModel
import org.purboyndradev.rt_rw.domain.repository.BannerRepository

class BannerRepositoryImpl(private val bannerApi: BannerApi) : BannerRepository {
    override suspend fun fetchAllBanners(
        paginationParams: PaginationParams?,
        queryParams: QueryParams?
    ): List<BannerModel> {
        val result = bannerApi.fetchAllBanners(
            paginationParams = paginationParams,
            queryParams = queryParams,
        )
        val banners = result.data?.map {
            it.toBannerModel()
        } ?: emptyList()
        return banners
    }
}