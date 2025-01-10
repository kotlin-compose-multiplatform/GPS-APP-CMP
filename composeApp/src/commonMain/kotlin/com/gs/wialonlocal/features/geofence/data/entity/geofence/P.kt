package com.gs.wialonlocal.features.geofence.data.entity.geofence

import kotlinx.serialization.Serializable

@Serializable
data class P(
    val r: Int,
    val x: Double,
    val y: Double
)