package com.gs.wialonlocal.features.settings.presentation.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.lyricist.strings
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.gs.wialonlocal.components.BackFragment
import org.jetbrains.compose.resources.painterResource
import wialonlocal.composeapp.generated.resources.Res
import wialonlocal.composeapp.generated.resources.menu

class NavigationBarSettings : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        BackFragment(
            modifier = Modifier.fillMaxSize(),
            onBackPresses = {
                navigator.pop()
            },
            title = strings.navigationBar
        ) {
            Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState())) {
                Text(
                    text = strings.visibleTabs,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = FontWeight.W500
                    ),
                    modifier = Modifier.padding(16.dp)
                )

                NavigationBarItem(
                    modifier = Modifier.fillMaxWidth(),
                    title = strings.monitoring
                )
                NavigationBarItem(
                    modifier = Modifier.fillMaxWidth(),
                    title = strings.map
                )
                NavigationBarItem(
                    modifier = Modifier.fillMaxWidth(),
                    title = strings.reports
                )
                NavigationBarItem(
                    modifier = Modifier.fillMaxWidth(),
                    title = strings.notifications
                )
                NavigationBarItem(
                    modifier = Modifier.fillMaxWidth(),
                    title = strings.geofences
                )
                NavigationBarItem(
                    modifier = Modifier.fillMaxWidth(),
                    title = strings.status
                )
                NavigationBarItem(
                    modifier = Modifier.fillMaxWidth(),
                    title = strings.settings
                )
                Text(
                    text = strings.hiddenTabs,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = FontWeight.W500
                    ),
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)
                )
                Box(Modifier.fillMaxWidth().padding(horizontal = 16.dp).border(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.inverseSurface,
                    shape = RoundedCornerShape(2.dp)
                ), contentAlignment = Alignment.Center) {
                    Text(
                        text = strings.dragToHide,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontWeight = FontWeight.W500
                        ),
                        modifier = Modifier.padding(16.dp)
                    )
                }
                Text(
                    text = strings.hideDescription,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = FontWeight.W500
                    ),
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
                )

            }
        }
    }
}

@Composable
fun NavigationBarItem(
    modifier: Modifier = Modifier,
    title: String
) {
    Column(modifier.background(
        color = MaterialTheme.colorScheme.surface
    )) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp, horizontal = 18.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = title,
                color = MaterialTheme.colorScheme.onBackground,
                style = MaterialTheme.typography.bodyLarge
            )

            Icon(
                painter = painterResource(Res.drawable.menu),
                contentDescription = "menu",
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(24.dp)
            )
        }
        HorizontalDivider()
    }
}