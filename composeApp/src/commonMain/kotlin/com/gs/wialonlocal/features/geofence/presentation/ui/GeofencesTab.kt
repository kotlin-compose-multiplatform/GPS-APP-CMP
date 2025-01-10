package com.gs.wialonlocal.features.geofence.presentation.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.Tab
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TabRow
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.lyricist.strings
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import cafe.adriel.voyager.transitions.SlideTransition
import com.gs.wialonlocal.features.main.presentation.ui.SearchBar
import com.gs.wialonlocal.features.main.presentation.ui.ToolBar
import com.gs.wialonlocal.features.monitoring.presentation.ui.monitoringlist.Groups
import kotlinx.coroutines.launch

object GeofencesTab : Tab {
    @OptIn(ExperimentalFoundationApi::class)
    @Composable
    override fun Content() {
        Navigator(GeoFenceScreen()) {
            SlideTransition(it)
        }
    }


    override val options: TabOptions
        @Composable
        get() {
            return TabOptions(
                index = 5U,
                title = strings.geofences
            )
        }
}