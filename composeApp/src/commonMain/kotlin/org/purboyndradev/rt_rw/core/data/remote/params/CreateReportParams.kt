package org.purboyndradev.rt_rw.core.data.remote.params

import kotlinx.serialization.Serializable

@Serializable
data class CreateReportParams(
    val title:String,
    val description: String,
    val image: ByteArray,
    val fileName: String
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false

        other as CreateReportParams

        if (title != other.title) return false
        if (description != other.description) return false
        if (!image.contentEquals(other.image)) return false
        if (fileName != other.fileName) return false

        return true
    }

    override fun hashCode(): Int {
        var result = title.hashCode()
        result = 31 * result + description.hashCode()
        result = 31 * result + image.contentHashCode()
        result = 31 * result + fileName.hashCode()
        return result
    }
}