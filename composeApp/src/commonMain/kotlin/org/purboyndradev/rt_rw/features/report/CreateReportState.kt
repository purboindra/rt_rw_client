package org.purboyndradev.rt_rw.features.report

data class CreateReportState(
    val title: String = "",
    val description: String = "",
    val capturedImageBytes: ByteArray? = null,

    val titleError: String? = null,
    val descriptionError: String? = null,

    val isSubmitting: Boolean = false
) {

    val canSubmit: Boolean
        get() = title.isNotBlank() && description.isNotBlank() && capturedImageBytes != null && !isSubmitting

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false

        other as CreateReportState

        if (title != other.title) return false
        if (description != other.description) return false
        if (!capturedImageBytes.contentEquals(other.capturedImageBytes)) return false
        if (title != other.title) return false

        return true
    }

    override fun hashCode(): Int {
        var result = title.hashCode()
        result = 31 * result + description.hashCode()
        result = 31 * result + (capturedImageBytes?.contentHashCode() ?: 0)
        result = 31 * result + title.hashCode()
        return result
    }
}