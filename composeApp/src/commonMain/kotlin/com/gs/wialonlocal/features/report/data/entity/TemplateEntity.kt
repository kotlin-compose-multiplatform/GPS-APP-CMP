package com.gs.wialonlocal.features.report.data.entity

import com.gs.wialonlocal.features.report.domain.model.TemplateModel
import kotlinx.serialization.Serializable

@Serializable
data class TemplateEntity(
    val id: String,
    val title: String
)
