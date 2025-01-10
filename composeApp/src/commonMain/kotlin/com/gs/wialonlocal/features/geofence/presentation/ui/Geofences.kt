package com.gs.wialonlocal.features.geofence.presentation.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
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
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material3.rememberDrawerState
import androidx.compose.material3.rememberStandardBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalViewConfiguration
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import cafe.adriel.lyricist.strings
import cafe.adriel.voyager.koin.koinNavigatorScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.gs.wialonlocal.common.CameraPosition
import com.gs.wialonlocal.common.GoogleMaps
import com.gs.wialonlocal.common.LatLong
import com.gs.wialonlocal.common.LatLongZoom
import com.gs.wialonlocal.common.defaultMapPos
import com.gs.wialonlocal.components.AppError
import com.gs.wialonlocal.components.ContextButton
import com.gs.wialonlocal.components.ContextMenu
import com.gs.wialonlocal.components.Empty
import com.gs.wialonlocal.features.geofence.data.entity.geofence.P
import com.gs.wialonlocal.features.geofence.data.entity.geofence.RealGeofenceApiItem
import com.gs.wialonlocal.features.geofence.presentation.viewmodel.GeofenceViewModel
import com.gs.wialonlocal.features.map.presentation.ui.MapContainer
import com.gs.wialonlocal.state.LocalAppSettings
import org.jetbrains.compose.resources.painterResource
import wialonlocal.composeapp.generated.resources.Res
import wialonlocal.composeapp.generated.resources.copy
import wialonlocal.composeapp.generated.resources.document
import wialonlocal.composeapp.generated.resources.ellipse
import wialonlocal.composeapp.generated.resources.geofence
import wialonlocal.composeapp.generated.resources.geofences_active
import wialonlocal.composeapp.generated.resources.invisible
import wialonlocal.composeapp.generated.resources.tile

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun Geofences(modifier: Modifier = Modifier) {
    val state = rememberStandardBottomSheetState(initialValue = SheetValue.Expanded)
    val scaffoldState = rememberBottomSheetScaffoldState(state)
    val navigator = LocalNavigator.currentOrThrow
    val viewModel: GeofenceViewModel = navigator.koinNavigatorScreenModel()
    val geofenceState = viewModel.geofenceState.collectAsState()
    LaunchedEffect(true) {
        viewModel.initGeoFences()
    }
    val mapType = LocalAppSettings.current
    BottomSheetScaffold(
        scaffoldState = scaffoldState,
        containerColor = MaterialTheme.colorScheme.surface,
        modifier = modifier,
        sheetDragHandle = {
            Row(
                Modifier.fillMaxWidth().background(
                    MaterialTheme.colorScheme.surface
                ).padding(12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Box(
                    Modifier.width(60.dp).height(3.dp).background(
                        MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
                        shape = RoundedCornerShape(3.dp)
                    )
                )
            }
        },
        sheetContainerColor = MaterialTheme.colorScheme.surface,
        contentColor = MaterialTheme.colorScheme.surface,
        sheetContentColor = MaterialTheme.colorScheme.surface,
        sheetPeekHeight = 300.dp,
        sheetShape = RoundedCornerShape(0.dp),
        sheetContent = {
            if(geofenceState.value.geofence.isNullOrEmpty()) {
                Empty(Modifier.fillMaxWidth())

            }
            if (geofenceState.value.loading) {
                LinearProgressIndicator(Modifier.fillMaxWidth())
            } else if (geofenceState.value.error.isNullOrEmpty().not()) {
                AppError(
                    modifier = Modifier.fillMaxSize(),
                    message = "Something went wrong"
                )
            } else {
                geofenceState.value.geofence?.let { list ->
                    if(list.isNotEmpty()) {
                        Column(
                            Modifier.fillMaxWidth().fillMaxHeight()
                                .verticalScroll(rememberScrollState())
                        ) {
                            repeat(list.count()) { index ->
                                val item = list[index]
                                GeofenceItem(modifier = Modifier.fillMaxWidth().clickable {
                                    navigator.push(GeofenceDetails(item))
                                }, item = list[index])
                            }
                        }
                    }
                }


            }




        },
        content = {
            Box(Modifier.fillMaxSize()) {
                geofenceState.value.geofence?.let {
                    Row(
                        modifier = Modifier.zIndex(20f).fillMaxWidth().background(
                            color = MaterialTheme.colorScheme.onSurface.copy(
                                alpha = 0.4f
                            )
                        ).padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            painter = painterResource(Res.drawable.tile),
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onPrimary
                        )
                        Text(
                            "0.0 ha",
                            style = MaterialTheme.typography.bodyLarge.copy(
                                fontWeight = FontWeight.W500
                            ),
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                        Icon(
                            painter = painterResource(Res.drawable.ellipse),
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onPrimary
                        )
                        Text(
                            "0.0 m",
                            style = MaterialTheme.typography.bodyLarge.copy(
                                fontWeight = FontWeight.W500
                            ),
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                }
                geofenceState.value.geofence?.let { list ->
                    if(list.isNotEmpty()) {
                        MapContainer(Modifier.fillMaxSize()) {

                            val geofences = emptyMap<String, List<P>>().toMutableMap()
                            list.forEach { g ->
                                geofences[g.n.plus(g.d)] = g.p
                            }
                            GoogleMaps(
                                modifier = Modifier.fillMaxSize(),
                                geofences = geofences,
                                mapType = mapType.value.mapType,
                                cameraPosition = CameraPosition(
                                    target = try {
                                        LatLong(
                                            geofences.values.first().first().y,
                                            geofences.values.first().first().x
                                        )
                                    } catch (ex: Exception) {
                                        defaultMapPos
                                    },
                                    zoom = 11f
                                )
                            )
                        }
                    }
                }


            }
        }
    )
}

@Composable
fun GeofenceItem(
    modifier: Modifier = Modifier,
    item: RealGeofenceApiItem
) {
    val open = remember {
        mutableStateOf(false)
    }
    val navigator = LocalNavigator.currentOrThrow
    val clipboard = LocalClipboardManager.current

    Row(
        modifier = modifier.background(
            color = MaterialTheme.colorScheme.surface
        ).padding(12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Icon(
            painter = painterResource(Res.drawable.geofence),
            contentDescription = "geofence",
            modifier = Modifier.size(40.dp).border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.inverseSurface,
                shape = CircleShape
            ).padding(12.dp),
            tint = MaterialTheme.colorScheme.primary
        )
        Text(
            modifier = Modifier.weight(1f),
            text = item.n,
            color = MaterialTheme.colorScheme.onBackground,
            style = MaterialTheme.typography.bodyMedium.copy(
                fontWeight = FontWeight.W500
            )
        )

        IconButton(
            onClick = {
                open.value = true
            }
        ) {
            ContextMenu(
                buttons = listOf(
                    ContextButton(
                        text = strings.showDescription,
                        icon = painterResource(Res.drawable.document),
                        onClick = {
                            navigator.push(GeofenceDetails(item))
                        }
                    ),
                    ContextButton(
                        text = strings.copyGeofences,
                        icon = painterResource(Res.drawable.copy),
                        onClick = {
                            clipboard.setText(
                                buildAnnotatedString {
                                    item.p.forEach { p ->
                                        append("${p.y},${p.x}")
                                    }
                                }
                            )
                        }
                    ),
                    ContextButton(
                        text = strings.makeInvisible,
                        icon = painterResource(Res.drawable.invisible),
                        onClick = {

                        }
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