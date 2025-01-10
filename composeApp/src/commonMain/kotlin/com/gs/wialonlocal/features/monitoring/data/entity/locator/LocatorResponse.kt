package com.gs.wialonlocal.features.monitoring.data.entity.locator

import kotlinx.serialization.Serializable

@Serializable
data class LocatorResponse(
    val app: String? = null,
    val at: Long? = null,
    val ct: Long? = null,
    val dur: Long? = null,
    val fl: Long? = null,
    val h: String? = null,
    val items: List<Int>? = null,
    val p: String? = null
)