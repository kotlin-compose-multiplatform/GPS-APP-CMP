package com.gs.wialonlocal.features.geofence.presentation.state

import com.gs.wialonlocal.features.geofence.data.entity.geofence.RealGeofenceApiItem

data class GeofenceState(
    val loading: Boolean = true,
    val error: String? = null,
    val geofence: List<RealGeofenceApiItem>? = null
)
