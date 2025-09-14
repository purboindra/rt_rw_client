package org.purboyndradev.rt_rw.core.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class BannerModel(
    val id: String,
    val placement: String,
    val title: String? = null,
    val subTitle: String? = null,
    val allText: String? = null,
    val imageUrl: String,
    val linkType: String,
    val linkUrl: String? = null,
    val links: String? = null,
    val platform: List<String>,
    val minAppVersion: String? = null,
)