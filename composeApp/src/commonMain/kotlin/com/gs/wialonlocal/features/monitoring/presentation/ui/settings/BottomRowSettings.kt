package com.gs.wialonlocal.features.monitoring.presentation.ui.settings

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
import com.gs.wialonlocal.components.CheckText
import com.gs.wialonlocal.components.RadioText
import com.gs.wialonlocal.components.SwitchText

class BottomRowSettings: Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        BackFragment(
            modifier = Modifier.fillMaxSize(),
            title = strings.bottomRow,
            onBackPresses = {
                navigator.pop()
            }
        ) {
            Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState())) {
                Spacer(Modifier.height(12.dp))
                CheckText(
                    modifier = Modifier.fillMaxWidth(),
                    text = strings.none,
                    onChange = { isChecked->

                    }
                )
                CheckText(
                    modifier = Modifier.fillMaxWidth(),
                    text = strings.address,
                    onChange = { isChecked->

                    }
                )
                CheckText(
                    modifier = Modifier.fillMaxWidth(),
                    text = strings.geofences,
                    onChange = { isChecked->

                    }
                )
            }
        }
    }
}