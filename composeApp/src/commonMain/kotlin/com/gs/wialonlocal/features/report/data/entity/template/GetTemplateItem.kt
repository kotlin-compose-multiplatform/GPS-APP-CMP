package com.gs.wialonlocal.features.report.data.entity.template

import kotlinx.serialization.Serializable

@Serializable
data class GetTemplateItem(
    val d: D,
    val f: Int,
    val i: Int
)