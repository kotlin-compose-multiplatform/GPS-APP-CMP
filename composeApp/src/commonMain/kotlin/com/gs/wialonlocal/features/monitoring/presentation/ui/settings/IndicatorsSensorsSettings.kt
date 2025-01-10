package com.gs.wialonlocal.features.monitoring.presentation.ui.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import cafe.adriel.lyricist.strings
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.gs.wialonlocal.components.BackFragment
import com.gs.wialonlocal.components.CheckText
import com.gs.wialonlocal.components.RadioText
import com.gs.wialonlocal.components.SwitchText
import com.gs.wialonlocal.features.main.presentation.ui.SearchBar

class IndicatorsSensorsSettings: Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        BackFragment(
            modifier = Modifier.fillMaxSize(),
            title = strings.indicatorsAndSensors,
            onBackPresses = {
                navigator.pop()
            },
            toolbar = {
                Box(Modifier.padding(16.dp).fillMaxWidth()) {
                    SearchBar(
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = strings.search,
                        onSearch = {

                        }
                    )
                }
            }
        ) {
            Column(Modifier.fillMaxSize().background(
                MaterialTheme.colorScheme.background
            ).verticalScroll(rememberScrollState())) {
                Spacer(Modifier.height(12.dp))
                Text(
                    strings.indicators,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = FontWeight.W500
                    ),
                    modifier = Modifier
                        .padding(horizontal = 16.dp),
                )
                Spacer(Modifier.height(12.dp))
                SwitchText(
                    modifier = Modifier.fillMaxWidth(),
                    text = strings.speed,
                    onChange = { isChecked->

                    }
                )
                SwitchText(
                    modifier = Modifier.fillMaxWidth(),
                    text = strings.ignition,
                    onChange = { isChecked->

                    }
                )
                SwitchText(
                    modifier = Modifier.fillMaxWidth(),
                    text = strings.motionParkingTime,
                    onChange = { isChecked->

                    }
                )
                SwitchText(
                    modifier = Modifier.fillMaxWidth(),
                    text = strings.motionParkingDuration,
                    onChange = { isChecked->

                    }
                )
                SwitchText(
                    modifier = Modifier.fillMaxWidth(),
                    text = strings.currentMileage,
                    onChange = { isChecked->

                    }
                )
                SwitchText(
                    modifier = Modifier.fillMaxWidth(),
                    text = strings.fuelLevel,
                    onChange = { isChecked->

                    }
                )
                Spacer(Modifier.height(12.dp))
                Text(
                    strings.sensors,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = FontWeight.W500
                    ),
                    modifier = Modifier
                        .padding(horizontal = 16.dp),
                )
                Spacer(Modifier.height(12.dp))
                SwitchText(
                    modifier = Modifier.fillMaxWidth(),
                    text = strings.battery,
                    onChange = { isChecked->

                    }
                )
                SwitchText(
                    modifier = Modifier.fillMaxWidth(),
                    text = strings.power,
                    onChange = { isChecked->

                    }
                )
                SwitchText(
                    modifier = Modifier.fillMaxWidth(),
                    text = "Зажигание",
                    onChange = { isChecked->

                    }
                )
            }
        }
    }
}