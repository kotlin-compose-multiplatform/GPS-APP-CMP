package com.gs.wialonlocal.features.notification.presentation.ui

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import cafe.adriel.lyricist.strings
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions

object NotificationTab: Tab {
    @Composable
    override fun Content() {
        Text("Notification")
    }

    override val options: TabOptions
        @Composable
        get() {
            return TabOptions(
                index = 4U,
                title = strings.notifications
            )
        }
}