package org.purboyndradev.rt_rw.features.report.presentation

import org.purboyndradev.rt_rw.core.domain.model.ReportModel

data class ReportDetailState(
    val isLoading: Boolean = false,
    val report: ReportModel? = null,
    val error: String? = null
)