package org.purboyndradev.rt_rw.core.data.remote.mapper

import org.purboyndradev.rt_rw.core.data.dto.BannerDto
import org.purboyndradev.rt_rw.core.domain.model.BannerModel

fun BannerDto.toModel(): BannerModel {
    return BannerModel(
        id = id,
        placement = placement,
        title = title,
        subTitle = subTitle,
        allText = allText,
        imageUrl = imageUrl,
        linkType = linkType,
        linkUrl = linkUrl,
        links = links,
        platform = platform,
        minAppVersion = minAppVersion,
    )
}