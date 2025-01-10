package com.gs.wialonlocal.features.monitoring.data.entity

import kotlinx.serialization.Serializable

@Serializable
data class GetUnits(
    val dataFlags: Long? = null,
    val indexFrom: Int? = null,
    val indexTo: Int? = null,
    val items: List<Item>? = null,
    val searchSpec: SearchSpec? = null,
    val totalItemsCount: Int? = null
)