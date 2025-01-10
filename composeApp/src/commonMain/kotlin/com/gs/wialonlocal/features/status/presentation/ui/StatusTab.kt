package com.gs.wialonlocal.features.status.presentation.ui

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import cafe.adriel.lyricist.strings
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import cafe.adriel.voyager.transitions.SlideTransition

object StatusTab: Tab {
    @Composable
    override fun Content() {
        Navigator(StatusScreen()) {navigator ->
            SlideTransition(navigator)
        }
    }

    override val options: TabOptions
        @Composable
        get() {
            return TabOptions(
                index = 6U,
                title = strings.status
            )
        }
}