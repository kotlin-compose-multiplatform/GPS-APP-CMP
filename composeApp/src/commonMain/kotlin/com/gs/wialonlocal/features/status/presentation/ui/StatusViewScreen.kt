package com.gs.wialonlocal.features.status.presentation.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import cafe.adriel.lyricist.strings
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.koinNavigatorScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import coil3.compose.LocalPlatformContext
import com.gs.wialonlocal.common.getUrlSharer
import com.gs.wialonlocal.common.openNavigationApp
import com.gs.wialonlocal.components.BackFragment
import com.gs.wialonlocal.components.CheckText
import com.gs.wialonlocal.components.ContextButton
import com.gs.wialonlocal.components.ContextMenu
import com.gs.wialonlocal.components.ImageLoader
import com.gs.wialonlocal.components.RadioText
import com.gs.wialonlocal.components.SwitchText
import com.gs.wialonlocal.features.main.presentation.ui.SearchBar
import com.gs.wialonlocal.features.monitoring.domain.model.UnitModel
import com.gs.wialonlocal.features.monitoring.presentation.ui.monitoringlist.LoadingDialog
import com.gs.wialonlocal.features.monitoring.presentation.ui.monitoringlist.LocatorDurationDialog
import com.gs.wialonlocal.features.monitoring.presentation.viewmodel.MonitoringViewModel
import org.jetbrains.compose.resources.painterResource
import wialonlocal.composeapp.generated.resources.Res
import wialonlocal.composeapp.generated.resources.copy_coordinate
import wialonlocal.composeapp.generated.resources.edit
import wialonlocal.composeapp.generated.resources.execute_report
import wialonlocal.composeapp.generated.resources.navigation_apps
import wialonlocal.composeapp.generated.resources.send_command
import wialonlocal.composeapp.generated.resources.share_location

class StatusViewScreen(
    private val ids: List<String>
) : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val monitoringViewModel: MonitoringViewModel = navigator.koinNavigatorScreenModel()
        val units = monitoringViewModel.units.collectAsState()
        val searchQuery = rememberSaveable {
            mutableStateOf("")
        }

        BackFragment(
            modifier = Modifier.fillMaxSize(),
            title = strings.unit,
            onBackPresses = {
                navigator.pop()
            },
            badge = {
                Box(
                    Modifier.background(
                        color = MaterialTheme.colorScheme.onSurface.copy(
                            alpha = 0.5f
                        ),
                        shape = CircleShape
                    ).padding(2.dp).width(17.dp).height(30.dp), contentAlignment = Alignment.Center
                ) {
                    Text(
                        units.value.data?.count {
                            ids.contains(it.id) && (it.carNumber.lowercase()
                                .contains(searchQuery.value.lowercase()) || it.address.lowercase()
                                .contains(searchQuery.value.lowercase()) || searchQuery.value.trim()
                                .isEmpty())
                        }?.toString() ?: "0",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onPrimary,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            },
            toolbar = {
                Box(Modifier.padding(16.dp).fillMaxWidth()) {
                    SearchBar(
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = strings.search,
                        onSearch = {
                            searchQuery.value = it
                        }
                    )
                }
            }
        ) {

            Column(
                Modifier.fillMaxSize().background(
                    MaterialTheme.colorScheme.background
                ).verticalScroll(rememberScrollState())
            ) {
                if (units.value.loading) {
                    LinearProgressIndicator(Modifier.fillMaxWidth())
                }
                units.value.data?.let { list ->
                    val filtered = list.filter {
                        ids.contains(it.id) && (it.carNumber.lowercase()
                            .contains(searchQuery.value.lowercase()) || it.address.lowercase()
                            .contains(searchQuery.value.lowercase()) || searchQuery.value.trim()
                            .isEmpty())
                    }
                    repeat(filtered.count()) { index ->
                        StatusCar(
                            modifier = Modifier.fillMaxWidth(),
                            unitModel = filtered[index]
                        )
                    }
                }

            }
        }
    }
}

@Composable
fun StatusCar(
    modifier: Modifier = Modifier,
    unitModel: UnitModel
) {
    val open = rememberSaveable {
        mutableStateOf(false)
    }
    val context = LocalPlatformContext.current

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
        onSelect = { duration ->
            viewModel.getLocatorUrl(duration, listOf(unitModel.id), onSuccess = { url ->
                getUrlSharer().shareUrl(url, context = context)
            })
        },
        onDismiss = {
            showDuration.value = false
        },
        show = showDuration.value
    )
    Row(
        modifier = modifier.background(
            color = MaterialTheme.colorScheme.surface
        ).padding(vertical = 6.dp, horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        ImageLoader(
            modifier = Modifier.size(40.dp).clip(CircleShape).border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.inverseSurface,
                shape = CircleShape
            ),
            url = unitModel.image,
            contentScale = ContentScale.FillBounds
        )

        Spacer(Modifier.width(12.dp))

        Text(
            unitModel.carNumber,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.weight(1f),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )


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
                            openNavigationApp(unitModel.latitude, unitModel.longitude, context)
                        }
                    ),
                    ContextButton(
                        text = strings.copyCoordinates,
                        icon = painterResource(Res.drawable.copy_coordinate),
                        enabled = true,
                        onClick = {
                            getUrlSharer().shareUrl(
                                "${unitModel.latitude}, ${unitModel.longitude}",
                                context = context
                            )
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
                contentDescription = "more",
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }


    }
}