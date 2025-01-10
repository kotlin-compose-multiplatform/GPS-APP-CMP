package com.gs.wialonlocal.features.geofence.data.entity

import kotlinx.serialization.Serializable

@Serializable
data class GeofenceApiItem(
    val d: D,
    val f: Long,
    val i: Long
)