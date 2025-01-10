package com.gs.wialonlocal.features.report.presentation.state

data class ExecuteReportState(
    val loading: Boolean = false,
    val error: String? = null,
    val pdf: ByteArray? = null
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false

        other as ExecuteReportState

        if (loading != other.loading) return false
        if (error != other.error) return false
        if (pdf != null) {
            if (other.pdf == null) return false
            if (!pdf.contentEquals(other.pdf)) return false
        } else if (other.pdf != null) return false

        return true
    }

    override fun hashCode(): Int {
        var result = loading.hashCode()
        result = 31 * result + (error?.hashCode() ?: 0)
        result = 31 * result + (pdf?.contentHashCode() ?: 0)
        return result
    }
}
