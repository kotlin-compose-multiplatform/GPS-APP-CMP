package com.gs.wialonlocal.features.geofence.presentation.viewmodel

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.gs.wialonlocal.core.network.Resource
import com.gs.wialonlocal.features.geofence.domain.usecase.GeofenceUseCase
import com.gs.wialonlocal.features.geofence.presentation.state.GeofenceState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class GeofenceViewModel(
    private val useCase: GeofenceUseCase
) : ScreenModel {
    private val _geofenceState = MutableStateFlow(GeofenceState())
    val geofenceState = _geofenceState.asStateFlow()

    fun initGeoFences() {
        if(_geofenceState.value.geofence.isNullOrEmpty()) {
            getGeoFences()
        }
    }

    fun getGeoFences() {
        screenModelScope.launch {
            useCase.getGeofence().onEach { result->
                when(result) {
                    is Resource.Error -> {
                        _geofenceState.value = _geofenceState.value.copy(
                            loading = false,
                            error = result.message,
                            geofence = result.data
                        )
                    }
                    is Resource.Loading -> {
                        _geofenceState.value = _geofenceState.value.copy(
                            loading = true,
                            error = result.message,
                            geofence = result.data
                        )
                    }
                    is Resource.Success -> {
                        _geofenceState.value = _geofenceState.value.copy(
                            loading = false,
                            error = null,
                            geofence = result.data
                        )
                    }
                }
            }.launchIn(this)
        }
    }
}