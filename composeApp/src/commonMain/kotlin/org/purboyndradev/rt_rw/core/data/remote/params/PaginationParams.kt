package org.purboyndradev.rt_rw.core.data.remote.params

import kotlinx.serialization.Serializable

@Serializable
data class PaginationParams(
    val page: Int,
    val limit: Int
)
