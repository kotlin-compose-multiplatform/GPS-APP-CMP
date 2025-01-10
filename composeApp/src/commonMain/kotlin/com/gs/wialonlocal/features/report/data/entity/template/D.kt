package com.gs.wialonlocal.features.report.data.entity.template

import kotlinx.serialization.Serializable

@Serializable
data class D(
    val cls: Int? = null,
    val drvrs: Drvrs? = null,
    val drvrsmax: Int? = null,
    val id: Int,
    val mu: Int? = null,
    val nm: String? = null,
    val rep: Map<String, Rep>? = null,
    val uacl: Long? = null
)