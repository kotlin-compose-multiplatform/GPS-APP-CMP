package com.gs.wialonlocal.features.monitoring.data.entity

import kotlinx.serialization.Serializable

@Serializable
data class SearchSpec(
    val itemsType: String? = null,
    val or_logic: String? = null,
    val propName: String? = null,
    val propType: String? = null,
    val propValueMask: String? = null,
    val sortType: String? = null
)