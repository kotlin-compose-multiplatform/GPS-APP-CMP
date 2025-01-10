package com.gs.wialonlocal.features.geofence.presentation.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import cafe.adriel.lyricist.strings
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.gs.wialonlocal.components.BackFragment
import com.gs.wialonlocal.features.geofence.data.entity.geofence.RealGeofenceApiItem

class GeofenceDetails(
    private val geofenceApiItem: RealGeofenceApiItem
):Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        BackFragment(
            title = geofenceApiItem.n,
            onBackPresses = {
                navigator.pop()
            }
        ) {
            Column(Modifier.fillMaxWidth().background(
                MaterialTheme.colorScheme.surface
            ).padding(16.dp)) {
                Text(
                    strings.description,
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontWeight = FontWeight.W500
                    ),
                    color = MaterialTheme.colorScheme.onBackground
                )
                Spacer(Modifier.height(6.dp))
                Text(
                    geofenceApiItem.d,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }
        }

    }
}