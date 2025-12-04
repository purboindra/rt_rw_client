package org.purboyndradev.rt_rw.features.report.presentation

import org.purboyndradev.rt_rw.core.domain.model.ReportModel


data class ReportState(
    val isLoading: Boolean = false,
    val reports: List<ReportModel> = emptyList(),
    val error: String? = null
)