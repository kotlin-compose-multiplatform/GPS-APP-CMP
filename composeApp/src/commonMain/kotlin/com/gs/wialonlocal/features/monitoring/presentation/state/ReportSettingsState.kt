package com.gs.wialonlocal.features.monitoring.presentation.state

import com.gs.wialonlocal.features.monitoring.data.entity.history.GetReportSettings

data class ReportSettingsState(
    val loading: Boolean = true,
    val error: String? = null,
    val settings: GetReportSettings? = null
)
