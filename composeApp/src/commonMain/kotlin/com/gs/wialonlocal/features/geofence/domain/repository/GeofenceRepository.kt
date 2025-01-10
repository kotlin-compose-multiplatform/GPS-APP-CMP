package com.gs.wialonlocal.features.geofence.domain.repository

import com.gs.wialonlocal.core.network.Resource
import com.gs.wialonlocal.features.geofence.data.entity.geofence.RealGeofenceApiItem
import kotlinx.coroutines.flow.Flow

interface GeofenceRepository {
    suspend fun getGeofence(): Flow<Resource<List<RealGeofenceApiItem>>>
}