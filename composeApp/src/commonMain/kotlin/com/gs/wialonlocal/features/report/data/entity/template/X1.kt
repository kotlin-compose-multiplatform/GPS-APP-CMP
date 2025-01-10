package com.gs.wialonlocal.features.report.data.entity.template

import kotlinx.serialization.Serializable

@Serializable
data class Rep(
    val c: Int,
    val ct: String,
    val id: Int,
    val n: String
)