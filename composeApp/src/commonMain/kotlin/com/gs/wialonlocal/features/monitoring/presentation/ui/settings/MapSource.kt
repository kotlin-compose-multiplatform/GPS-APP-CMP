package com.gs.wialonlocal.features.monitoring.presentation.ui.settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.lyricist.strings
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.gs.wialonlocal.components.BackFragment
import com.gs.wialonlocal.components.RadioText
import com.gs.wialonlocal.components.SwitchText
import com.gs.wialonlocal.features.settings.data.settings.AppSettings
import com.gs.wialonlocal.features.settings.data.settings.MapType
import com.gs.wialonlocal.state.LocalAppSettings
import org.koin.compose.koinInject

class MapSource(
    private val isBack: Boolean = true
) : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val appSettings = LocalAppSettings.current
        val settings = koinInject<AppSettings>()

        fun onSelected() {
            if (isBack) {
                navigator.pop()
            }
        }

        BackFragment(
            modifier = Modifier.fillMaxSize(),
            title = strings.mapSource,
            onBackPresses = {
                navigator.pop()
            }
        ) {
            Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState())) {
                RadioText(
                    modifier = Modifier.fillMaxWidth().clickable {
                        appSettings.value = appSettings.value.copy(
                            mapType = MapType.MAP
                        )
                        settings.saveMapType("map")
                        onSelected()
                    },
                    initial = appSettings.value.mapType == MapType.MAP,
                    text = "Google Default",
                    onChange = { isChecked ->
                        if (isChecked) {
                            appSettings.value = appSettings.value.copy(
                                mapType = MapType.MAP
                            )
                            settings.saveMapType("map")
                            onSelected()
                        }
                    }
                )
                RadioText(
                    modifier = Modifier.fillMaxWidth().clickable {
                        appSettings.value = appSettings.value.copy(
                            mapType = MapType.SATELLITE
                        )
                        settings.saveMapType("satellite")
                        onSelected()
                    },
                    initial = appSettings.value.mapType == MapType.SATELLITE,
                    text = "Google Satellite",
                    onChange = { isChecked ->
                        if (isChecked) {
                            appSettings.value = appSettings.value.copy(
                                mapType = MapType.SATELLITE
                            )
                            settings.saveMapType("satellite")
                            onSelected()
                        }
                    }
                )
                RadioText(
                    modifier = Modifier.fillMaxWidth().clickable {
                        appSettings.value = appSettings.value.copy(
                            mapType = MapType.HYBRID
                        )
                        settings.saveMapType("hybrid")
                        onSelected()
                    },
                    initial = appSettings.value.mapType == MapType.HYBRID,
                    text = "Google Hybrid",
                    onChange = { isChecked ->
                        if (isChecked) {
                            appSettings.value = appSettings.value.copy(
                                mapType = MapType.HYBRID
                            )
                            settings.saveMapType("hybrid")
                            onSelected()
                        }
                    }
                )
            }
        }
    }
}