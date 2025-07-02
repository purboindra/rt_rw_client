package org.purboyndradev.rt_rw.core.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class ResponseDto<T>(
    val code: String? = null,
    val message: String,
    val data: T? = null,
)