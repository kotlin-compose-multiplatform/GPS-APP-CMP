package com.gs.wialonlocal.features.report.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class TemplateModel(
    val id: String,
    val name: String,
    val resourceId: Int,
    val resourceD: Int,
    val templateC: Int,
    val templateCt: String,
    val uacl: Long
)
