package com.gs.wialonlocal.features.monitoring.presentation.ui

import androidx.compose.runtime.Composable
import cafe.adriel.lyricist.strings
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import com.gs.wialonlocal.features.monitoring.presentation.ui.MonitoringRouter

object MonitoringTab: Tab {
    @Composable
    override fun Content() {
        MonitoringRouter()
    }

    override val options: TabOptions
        @Composable
        get() {
            return TabOptions(
                index = 1U,
                title = strings.monitoring
            )
        }
}