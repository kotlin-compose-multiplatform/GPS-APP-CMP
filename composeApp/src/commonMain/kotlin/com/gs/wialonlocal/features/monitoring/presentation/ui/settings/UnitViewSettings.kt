package com.gs.wialonlocal.features.monitoring.presentation.ui.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.unit.dp
import cafe.adriel.lyricist.strings
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.gs.wialonlocal.components.BackFragment
import com.gs.wialonlocal.components.RadioText
import com.gs.wialonlocal.components.SwitchText

class UnitViewSettings: Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        BackFragment(
            modifier = Modifier.fillMaxSize(),
            title = strings.unitView,
            onBackPresses = {
                navigator.pop()
            }
        ) {
            Column(Modifier.fillMaxSize().background(
                MaterialTheme.colorScheme.background
            ).verticalScroll(rememberScrollState())) {
                Spacer(Modifier.height(16.dp))
                SwitchText(
                    modifier = Modifier.fillMaxWidth().clickable {
                        navigator.push(TitleSettings())
                    },
                    text = strings.title,
                    subTitle = strings.unitName,
                    arrow = true
                )

                SwitchText(
                    modifier = Modifier.fillMaxWidth().clickable {
                        navigator.push(SubtitleSettings())
                    },
                    text = strings.subTitle,
                    subTitle = strings.driverName,
                    arrow = true
                )

                Spacer(Modifier.height(16.dp))
                SwitchText(
                    modifier = Modifier.fillMaxWidth().clickable {
                        navigator.push(IndicatorsSensorsSettings())
                    },
                    text = strings.indicatorsAndSensors,
                    subTitle = "Speed, Ignition, Motion and parking duration, Mileage in current trip, Fuel level",
                    arrow = true
                )
                Spacer(Modifier.height(16.dp))
                SwitchText(
                    modifier = Modifier.fillMaxWidth().clickable {
                        navigator.push(BottomRowSettings())
                    },
                    text = strings.bottomRow,
                    subTitle = strings.address,
                    arrow = true
                )
            }
        }
    }
}