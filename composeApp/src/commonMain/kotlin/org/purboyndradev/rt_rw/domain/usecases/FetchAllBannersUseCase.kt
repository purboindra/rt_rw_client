package org.purboyndradev.rt_rw.domain.usecases

import org.purboyndradev.rt_rw.core.data.remote.mapper.toBannerModel
import org.purboyndradev.rt_rw.core.domain.AppError
import org.purboyndradev.rt_rw.core.domain.Result
import org.purboyndradev.rt_rw.core.domain.model.BannerModel
import org.purboyndradev.rt_rw.domain.repository.BannerRepository

class FetchAllBannersUseCase(private val bannerRepository: BannerRepository) {
    suspend operator fun invoke(): Result<List<BannerModel>, AppError> {
        return when (val result = bannerRepository.fetchAllBanners()) {
            is Result.Success -> {
                val data = result.data
                if (data.data == null) return Result.Error(AppError.Remote.NotFound)
                var banners: List<BannerModel> = emptyList()
                data.data.forEach {
                    banners = banners + it.toBannerModel()
                }
                Result.Success(banners)
            }

            is Result.Error -> {
                val error = result.error
                Result.Error(error)
            }
        }
    }
}