package org.purboyndradev.rt_rw.domain.repository

import org.purboyndradev.rt_rw.core.data.remote.params.PaginationParams
import org.purboyndradev.rt_rw.core.data.remote.params.QueryParams
import org.purboyndradev.rt_rw.core.domain.model.BannerModel

interface BannerRepository {
    suspend fun fetchAllBanners(
        paginationParams: PaginationParams? = null,
        queryParams: QueryParams? = null
    ): List<BannerModel>
}