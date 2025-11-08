package org.purboyndradev.rt_rw.domain.usecases

import org.purboyndradev.rt_rw.core.domain.AppError
import org.purboyndradev.rt_rw.core.domain.Result
import org.purboyndradev.rt_rw.core.domain.model.BannerModel
import org.purboyndradev.rt_rw.core.network.mapKtorExceptionToAppError
import org.purboyndradev.rt_rw.domain.repository.BannerRepository

class FetchAllBannersUseCase(private val bannerRepository: BannerRepository) {
    suspend operator fun invoke(): Result<List<BannerModel>, AppError> {
        return try {
            val result = bannerRepository.fetchAllBanners()
            val banners = result
            Result.Success(banners)
        } catch (e: Exception) {
            Result.Error(mapKtorExceptionToAppError(e))
        }
    }
}