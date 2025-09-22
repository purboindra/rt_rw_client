package org.purboyndradev.rt_rw.core.data.remote.mapper

import org.purboyndradev.rt_rw.core.data.dto.NewsDto
import org.purboyndradev.rt_rw.core.domain.model.NewsModel

fun NewsDto.toNewsModel(): NewsModel {
    return NewsModel(
        id = id,
        title = title,
        description = description,
        body = body,
        createdAt = createdAt,
        rt = rt,
        author = author
    )
}