package com.gs.wialonlocal.features.map.presentation.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.lyricist.strings
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.koinNavigatorScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.gs.wialonlocal.common.GoogleMaps
import com.gs.wialonlocal.common.LatLong
import com.gs.wialonlocal.common.LatLongZoom
import com.gs.wialonlocal.components.AppError
import com.gs.wialonlocal.components.AppLoading
import com.gs.wialonlocal.features.geofence.data.entity.geofence.P
import com.gs.wialonlocal.features.geofence.presentation.viewmodel.GeofenceViewModel
import com.gs.wialonlocal.features.main.presentation.ui.SearchBar
import com.gs.wialonlocal.features.main.presentation.ui.ToolBar
import com.gs.wialonlocal.features.monitoring.presentation.ui.unit.UnitScreen
import com.gs.wialonlocal.features.monitoring.presentation.viewmodel.MonitoringViewModel
import com.gs.wialonlocal.state.LocalAppSettings

class MapScreen : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val viewModel = navigator.koinNavigatorScreenModel<MonitoringViewModel>()
        val units = viewModel.units.collectAsState()
        val geoFenceViewModel: GeofenceViewModel = navigator.koinNavigatorScreenModel()
        val geofenceState = geoFenceViewModel.geofenceState.collectAsState()
        val mapType = LocalAppSettings.current
        val searchQuery = rememberSaveable {
            mutableStateOf("")
        }
        LaunchedEffect(true) {
            viewModel.initUnits(requireCheckUpdate = true)
            geoFenceViewModel.initGeoFences()
        }
        Column(Modifier.fillMaxSize()) {
            ToolBar {
                Box(Modifier.padding(16.dp)) {
                    SearchBar(
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = strings.search,
                        onSearch = {
                            searchQuery.value = it
                        }
                    )
                }
            }

            if (units.value.loading) {
                AppLoading(Modifier.fillMaxSize())
            } else if (units.value.error.isNullOrEmpty().not()) {
                AppError(
                    Modifier.fillMaxSize(),
                    message = units.value.error
                )
            }
            units.value.data?.let { units ->
                val filtered = units.filter {
                    it.carNumber.lowercase()
                        .contains(searchQuery.value.lowercase()) || it.address.lowercase()
                        .contains(searchQuery.value.lowercase()) || searchQuery.value.isEmpty()
                }
                val geofences = emptyMap<String, List<P>>().toMutableMap()
                geofenceState.value.geofence?.forEach { g ->
                    geofences[g.n.plus(g.d)] = g.p
                }
                MapContainer {
                    GoogleMaps(
                        modifier = Modifier.fillMaxSize(),
                        units = filtered,
                        geofences = geofences,
                        onUnitClick = { unit ->
                            navigator.push(UnitScreen(unit.id, unit))
                        },
                        mapType = mapType.value.mapType
                    )
                }
            }

        }
    }
}