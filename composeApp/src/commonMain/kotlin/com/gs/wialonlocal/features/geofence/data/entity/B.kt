package com.gs.wialonlocal.features.geofence.data.entity

import kotlinx.serialization.Serializable

@Serializable
data class B(
    val cen_x: Double,
    val cen_y: Double,
    val max_x: Double,
    val max_y: Double,
    val min_x: Double,
    val min_y: Double
)