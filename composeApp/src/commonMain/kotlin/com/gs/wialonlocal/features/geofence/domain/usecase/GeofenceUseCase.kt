package com.gs.wialonlocal.features.geofence.domain.usecase

import com.gs.wialonlocal.core.network.Resource
import com.gs.wialonlocal.features.geofence.data.entity.geofence.RealGeofenceApiItem
import com.gs.wialonlocal.features.geofence.domain.repository.GeofenceRepository
import kotlinx.coroutines.flow.Flow

class GeofenceUseCase(
    private val repository: GeofenceRepository
) {
    suspend fun getGeofence(): Flow<Resource<List<RealGeofenceApiItem>>> {
        return repository.getGeofence()
    }
}