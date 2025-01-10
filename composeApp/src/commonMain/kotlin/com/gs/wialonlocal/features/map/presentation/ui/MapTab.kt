package com.gs.wialonlocal.features.map.presentation.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import cafe.adriel.lyricist.strings
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import cafe.adriel.voyager.transitions.SlideTransition
import com.gs.wialonlocal.common.GoogleMaps

object MapTab: Tab {
    @Composable
    override fun Content() {
        Navigator(MapScreen()){ navigator ->
            SlideTransition(navigator)
        }
    }

    override val options: TabOptions
        @Composable
        get() {
            return TabOptions(
                index = 2U,
                title = strings.map
            )
        }
}