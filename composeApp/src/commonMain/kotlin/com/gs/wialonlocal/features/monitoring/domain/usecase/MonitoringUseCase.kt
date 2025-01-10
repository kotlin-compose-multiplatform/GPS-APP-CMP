package com.gs.wialonlocal.features.monitoring.domain.usecase

import com.gs.wialonlocal.core.network.Resource
import com.gs.wialonlocal.features.monitoring.data.entity.TripDetector
import com.gs.wialonlocal.features.monitoring.data.entity.hardware.HardwareTypeEntity
import com.gs.wialonlocal.features.monitoring.data.entity.history.CustomFields
import com.gs.wialonlocal.features.monitoring.data.entity.history.GetReportSettings
import com.gs.wialonlocal.features.monitoring.data.entity.history.LoadEventRequest
import com.gs.wialonlocal.features.monitoring.data.entity.history.Trip
import com.gs.wialonlocal.features.monitoring.data.entity.locator.LocatorResponse
import com.gs.wialonlocal.features.monitoring.domain.model.UnitModel
import com.gs.wialonlocal.features.monitoring.domain.repository.MonitoringRepository
import kotlinx.coroutines.flow.Flow

class MonitoringUseCase(private val repository: MonitoringRepository) {
    suspend fun getEvents(isAddressRequired: Boolean = true): Flow<Resource<List<UnitModel>>> {
        return repository.getEvents(isAddressRequired = isAddressRequired)
    }
    suspend fun getUpdates(oldEvents: List<UnitModel>): Flow<Resource<List<UnitModel>>> {
        return repository.getUpdates(oldEvents)
    }
    suspend fun getReportSettings(itemId: String): Flow<Resource<GetReportSettings>> {
        return repository.getReportSettings(itemId)
    }

    suspend fun loadEvents(req: LoadEventRequest): Flow<Resource<Pair<List<Trip>, List<Trip>>>> {
        return repository.loadEvents(req)
    }

    suspend fun unloadEvents(id: String): Flow<Resource<Unit>> {
        return repository.unloadEvents(id)
    }

    suspend fun getEvent(id: String,mode: Int = 0): Flow<Resource<CustomFields>> {
        return repository.getEvent(id,mode)
    }

    suspend fun getLocatorUrl(duration: Long, items: List<String>): Flow<Resource<LocatorResponse>> {
        return repository.getLocatorUrl(duration, items)
    }
    suspend fun getTripDetector(): Flow<Resource<TripDetector>> {
        return repository.getTripDetector()
    }
    suspend fun getHardwareTypes(): Flow<Resource<List<HardwareTypeEntity>>> {
        return repository.getHardwareTypes()
    }
    suspend fun getAddress(latitude: Double, longitude: Double): Flow<Resource<String>> {
        return repository.getAddress(latitude, longitude)
    }
}