package com.gs.wialonlocal.features.geofence.data.entity

import kotlinx.serialization.Serializable

@Serializable
data class D(
    val zg: Zg,
    val zl: Map<String, Zl>
)