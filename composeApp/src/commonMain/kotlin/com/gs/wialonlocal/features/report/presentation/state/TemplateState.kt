package com.gs.wialonlocal.features.report.presentation.state

import com.gs.wialonlocal.features.report.domain.model.TemplateModel

data class TemplateState(
    val loading: Boolean = true,
    val error: String? = null,
    val templates: List<TemplateModel>? = null
)
