package com.gs.wialonlocal.features.monitoring.presentation.ui

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.transitions.SlideTransition
import com.gs.wialonlocal.features.monitoring.presentation.ui.monitoringlist.MonitoringListScreen

@Composable
fun MonitoringRouter() {
    Navigator(MonitoringListScreen())
}