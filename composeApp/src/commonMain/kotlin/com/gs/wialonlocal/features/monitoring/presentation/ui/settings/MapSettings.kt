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
import com.gs.wialonlocal.components.SwitchText

class MapSettings: Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        BackFragment(
            modifier = Modifier.fillMaxSize(),
            title = strings.mapSettings,
            onBackPresses = {
                navigator.pop()
            }
        ) {
            Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState())) {
                SwitchText(
                    modifier = Modifier.fillMaxWidth().clickable {
                        navigator.push(MapSource())
                    },
                    text = strings.mapSource,
                    arrow = true
                )
                repeat(20) {
                    SwitchText(
                        modifier = Modifier.fillMaxWidth(),
                        text = strings.traffic,
                        onChange = { isChecked->

                        }
                    )
                }
            }
        }
    }
}