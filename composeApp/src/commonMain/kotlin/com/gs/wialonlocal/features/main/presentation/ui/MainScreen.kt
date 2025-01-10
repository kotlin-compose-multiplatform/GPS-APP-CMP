package com.gs.wialonlocal.features.main.presentation.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContent
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.Scaffold
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.tab.CurrentTab
import cafe.adriel.voyager.navigator.tab.TabNavigator
import com.gs.wialonlocal.features.monitoring.presentation.ui.MonitoringTab

class MainScreen : Screen {
    @Composable
    override fun Content() {
        TabNavigator(MonitoringTab) {
            Scaffold(
                backgroundColor = MaterialTheme.colorScheme.background,
                bottomBar = {
                    BottomBar()
                }
            ) {
                Box(Modifier.padding(bottom = 75.dp)) {
                    CurrentTab()
                }
            }

        }

    }
}