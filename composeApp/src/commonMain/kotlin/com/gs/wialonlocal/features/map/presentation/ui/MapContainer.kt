package com.gs.wialonlocal.features.map.presentation.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.gs.wialonlocal.features.monitoring.presentation.ui.settings.MapSettings
import com.gs.wialonlocal.features.monitoring.presentation.ui.settings.MapSource
import org.jetbrains.compose.resources.painterResource
import wialonlocal.composeapp.generated.resources.Res
import wialonlocal.composeapp.generated.resources.settings_active

@Composable
fun MapContainer(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    val navigator = LocalNavigator.currentOrThrow
    Box(modifier) {
        IconButton(
            onClick = {
                navigator.push(MapSource())
            },
            colors = IconButtonDefaults.iconButtonColors(
                containerColor = MaterialTheme.colorScheme.surface
            ),
            modifier = Modifier.zIndex(20f).offset(x = 16.dp, y = 16.dp)
        ) {
            Icon(
                painter = painterResource(Res.drawable.settings_active),
                contentDescription = "map settings",
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        content()
    }
}