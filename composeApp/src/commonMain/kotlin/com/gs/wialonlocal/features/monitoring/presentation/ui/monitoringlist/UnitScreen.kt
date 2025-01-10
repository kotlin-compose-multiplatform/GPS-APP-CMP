package com.gs.wialonlocal.features.monitoring.presentation.ui.monitoringlist

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import cafe.adriel.lyricist.strings
import cafe.adriel.voyager.koin.koinNavigatorScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import coil3.compose.LocalPlatformContext
import com.gs.wialonlocal.common.getUrlSharer
import com.gs.wialonlocal.common.openNavigationApp
import com.gs.wialonlocal.components.AppError
import com.gs.wialonlocal.components.AppLoading
import com.gs.wialonlocal.components.ContextButton
import com.gs.wialonlocal.components.ContextMenu
import com.gs.wialonlocal.components.ImageLoader
import com.gs.wialonlocal.features.monitoring.data.settings.UnitsSettings
import com.gs.wialonlocal.features.monitoring.domain.model.UnitModel
import com.gs.wialonlocal.features.monitoring.presentation.ui.unit.UnitScreen
import com.gs.wialonlocal.features.monitoring.presentation.viewmodel.MonitoringViewModel
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.koinInject
import wialonlocal.composeapp.generated.resources.Res
import wialonlocal.composeapp.generated.resources.clock
import wialonlocal.composeapp.generated.resources.copy_coordinate
import wialonlocal.composeapp.generated.resources.distance
import wialonlocal.composeapp.generated.resources.edit
import wialonlocal.composeapp.generated.resources.execute_report
import wialonlocal.composeapp.generated.resources.forward
import wialonlocal.composeapp.generated.resources.key
import wialonlocal.composeapp.generated.resources.key_off
import wialonlocal.composeapp.generated.resources.navigation_apps
import wialonlocal.composeapp.generated.resources.send_command
import wialonlocal.composeapp.generated.resources.share_location

@Composable
fun Units(searchQuery: String) {
    val navigator = LocalNavigator.currentOrThrow
    val viewModel = navigator.koinNavigatorScreenModel<MonitoringViewModel>()
    val units = viewModel.units.collectAsState()
    val unitSettings = koinInject<UnitsSettings>()
    val selectedIds = unitSettings.getUnits()
    LaunchedEffect(true) {
        viewModel.initUnits(requireCheckUpdate = true)
    }
    if (units.value.loading) {
        AppLoading(Modifier.fillMaxSize())
    } else if (units.value.error.isNullOrEmpty().not()) {
        AppError(Modifier.fillMaxSize(), units.value.error)
    } else {
        units.value.data?.let { list ->
            val filtered = list.filter {
                it.carNumber.lowercase().contains(searchQuery.lowercase()) || it.address.lowercase().contains(searchQuery.lowercase()) || searchQuery.trim().isEmpty()
            }.filter { selectedIds.contains(it.id) }
            LazyColumn {
                items(filtered) { item ->
                    if(item.address.isEmpty()) {
                        viewModel.getAddress(item.latitude, item.longitude, item.id)
                    }
                    UnitItem(
                        modifier = Modifier.clickable {
                            navigator.push(UnitScreen(item.id, item))
                        },
                        item = item
                    )
                }
            }
        }
    }
}

@Composable
internal fun UnitItem(
    modifier: Modifier = Modifier,
    item: UnitModel
) {
    val context = LocalPlatformContext.current

    val open = remember {
        mutableStateOf(false)
    }
    val navigator = LocalNavigator.currentOrThrow
    val viewModel: MonitoringViewModel = navigator.koinNavigatorScreenModel()

    val showDuration = remember {
        mutableStateOf(false)
    }

    val locatorState = viewModel.locatorState.collectAsState()

    LoadingDialog(
        Modifier.fillMaxSize(),
        loading = locatorState.value.loading
    )




    LocatorDurationDialog(
        onSelect = { duration->
            viewModel.getLocatorUrl(duration, listOf(item.id), onSuccess = {url->
                getUrlSharer().shareUrl(url, context = context)
            })
        },
        onDismiss = {
            showDuration.value = false
        },
        show = showDuration.value
    )

    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(
                color = MaterialTheme.colorScheme.surface
            )
            .padding(top = 8.dp)

    ) {
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
            ) {
                Row(Modifier.padding(start = 16.dp)) {
                    ImageLoader(
                        modifier = Modifier.size(40.dp),
                        url = item.image,
                        contentScale = ContentScale.FillBounds
                    )
                }

                val shape = RoundedCornerShape(
                    topEnd = 4.dp,
                    bottomEnd = 4.dp
                )
                Spacer(Modifier.height(12.dp))
                Box(
                    modifier = Modifier.defaultMinSize(minWidth = 80.dp).clip(shape).background(
                        color = item.calculateDifference().second.copy(alpha = 0.5f),
                        shape = shape
                    ).padding(
                        horizontal = 24.dp,
                        vertical = 4.dp
                    )
                ) {
                    Text(
                        text = item.calculateDifference().first,
                        style = MaterialTheme.typography.bodySmall.copy(
                            fontWeight = FontWeight.W400
                        ),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }


            }
            Spacer(Modifier.width(8.dp))
            Column(Modifier.weight(1f)) {
                Text(
                    text = item.number,
                    color = MaterialTheme.colorScheme.onSurface,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = FontWeight.W500
                    )
                )
                Spacer(Modifier.height(6.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(2.dp)
                ) {
                    if (item.isOnline) {
                        Row(
                            modifier = Modifier.height(30.dp).background(
                                color = MaterialTheme.colorScheme.secondary,
                                shape = RoundedCornerShape(6.dp)
                            ).padding(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                painter = painterResource(Res.drawable.forward),
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onPrimary
                            )
                            Spacer(Modifier.width(6.dp))
                            Text(
                                text = item.speed,
                                color = MaterialTheme.colorScheme.onPrimary,
                                style = MaterialTheme.typography.bodySmall.copy(
                                    fontWeight = FontWeight.W500
                                )
                            )
                        }
                    }

                    Row(
                        modifier = Modifier.height(30.dp).background(
                            color = if(item.ignitionOn) MaterialTheme.colorScheme.primaryContainer.copy(
                                alpha = 0.1f
                            ) else MaterialTheme.colorScheme.onSurfaceVariant.copy(
                                alpha = 0.2f
                            ),
                            shape = RoundedCornerShape(6.dp)
                        ).padding(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            painter = painterResource(if (item.ignitionOn) Res.drawable.key else Res.drawable.key_off),
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.secondary
                        )
                    }

                    Row(
                        modifier = Modifier.height(30.dp).background(
                            color = MaterialTheme.colorScheme.primaryContainer.copy(
                                alpha = 0.1f
                            ),
                            shape = RoundedCornerShape(6.dp)
                        ).padding(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            painter = painterResource(Res.drawable.clock),
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary
                        )
                        Spacer(Modifier.width(6.dp))
                        Text(
                            text = item.convertTime(),
                            color = MaterialTheme.colorScheme.onSurface,
                            style = MaterialTheme.typography.bodySmall.copy(
                                fontWeight = FontWeight.W500
                            )
                        )
                    }

                   if(item.isOnline) {
                       Row(
                           modifier = Modifier.height(30.dp).background(
                               color = MaterialTheme.colorScheme.primaryContainer.copy(
                                   alpha = 0.1f
                               ),
                               shape = RoundedCornerShape(6.dp)
                           ).padding(8.dp),
                           verticalAlignment = Alignment.CenterVertically
                       ) {
                           Icon(
                               painter = painterResource(Res.drawable.distance),
                               contentDescription = null,
                               tint = MaterialTheme.colorScheme.primary
                           )
                           Spacer(Modifier.width(6.dp))
                           Text(
                               text = item.estimateDistance,
                               color = MaterialTheme.colorScheme.onSurface,
                               style = MaterialTheme.typography.bodySmall.copy(
                                   fontWeight = FontWeight.W500
                               )
                           )
                       }
                   }
                }
                Spacer(Modifier.height(6.dp))
                Text(
                    text = item.address,
                    color = MaterialTheme.colorScheme.outline,
                    style = MaterialTheme.typography.bodySmall
                )
            }
            IconButton(
                onClick = {
                    open.value = true
                }
            ) {
                ContextMenu(
                    buttons = listOf(
                        ContextButton(
                            text = strings.sendCommands,
                            icon = painterResource(Res.drawable.send_command),
                            enabled = false,
                            onClick = {}
                        ),
                        ContextButton(
                            text = strings.shareLocation,
                            icon = painterResource(Res.drawable.share_location),
                            enabled = true,
                            onClick = {
                                showDuration.value = true
                            }
                        ),
                        ContextButton(
                            text = strings.navigationApps,
                            icon = painterResource(Res.drawable.navigation_apps),
                            enabled = true,
                            onClick = {
                                openNavigationApp(item.latitude, item.longitude, context)
                            }
                        ),
                        ContextButton(
                            text = strings.copyCoordinates,
                            icon = painterResource(Res.drawable.copy_coordinate),
                            enabled = true,
                            onClick = {
                                getUrlSharer().shareUrl("${item.latitude}, ${item.longitude}", context = context)
                            }
                        ),
                        ContextButton(
                            text = strings.executeReports,
                            icon = painterResource(Res.drawable.execute_report),
                            enabled = false,
                            onClick = {}
                        ),
                        ContextButton(
                            text = strings.edit,
                            icon = painterResource(Res.drawable.edit),
                            enabled = false,
                            onClick = {}
                        )
                    ),
                    open = open.value,
                    onDismiss = {
                        open.value = false
                    }
                )
                Icon(
                    Icons.Default.MoreVert,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
        Spacer(Modifier.height(8.dp))
        HorizontalDivider(
            color = MaterialTheme.colorScheme.inverseSurface,
            thickness = 1.dp
        )
    }
}

@Composable
fun LocatorDurationDialog(
    onSelect: (Long) -> Unit,
    show: Boolean = false,
    onDismiss: () -> Unit
) {
    val items = listOf<Pair<String, Long>>(
        Pair("1 gun", 86400),
        Pair("3 sagat", 10800),
        Pair("3 gun", 259200),
        Pair("Hepde", 604800),
        Pair("Ay", 8640000),
    )
    if(show) {
        Dialog(
            onDismissRequest = {
                onDismiss()
            }
        ) {
            Column(Modifier.fillMaxWidth().background(
                color = MaterialTheme.colorScheme.surface
            ).padding(12.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("Select duration:", style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.onSurface)
                repeat(items.count()) { index->
                    val item = items[index]
                    androidx.compose.material3.TextButton(
                        modifier = Modifier.fillMaxWidth(),
                        onClick = {
                            onDismiss()
                            onSelect(item.second)
                        }
                    ) {
                        Text(item.first)
                    }
                }
            }
        }
    }

}

@Composable
fun LoadingDialog(modifier: Modifier = Modifier, loading: Boolean = false) {
    if(loading) {
        Dialog(
            onDismissRequest = {}
        ) {
            Box(Modifier.fillMaxSize()) {
                CircularProgressIndicator(Modifier.size(35.dp).align(Alignment.Center))
            }
        }
    }
}