package com.gs.wialonlocal.features.monitoring.presentation.ui.history

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.absoluteOffset
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import cafe.adriel.lyricist.strings
import cafe.adriel.voyager.koin.koinNavigatorScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.gs.wialonlocal.common.LatLong
import com.gs.wialonlocal.features.monitoring.data.entity.history.Trip
import com.gs.wialonlocal.features.monitoring.data.entity.history.convertTimestampToDateTime
import com.gs.wialonlocal.features.monitoring.presentation.viewmodel.MonitoringViewModel
import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import kotlinx.datetime.atTime
import kotlinx.datetime.minus
import kotlinx.datetime.plus
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import org.jetbrains.compose.resources.painterResource
import wialonlocal.composeapp.generated.resources.Res
import wialonlocal.composeapp.generated.resources.circle_car
import wialonlocal.composeapp.generated.resources.circle_graph
import wialonlocal.composeapp.generated.resources.circle_parking
import wialonlocal.composeapp.generated.resources.clock
import wialonlocal.composeapp.generated.resources.key
import wialonlocal.composeapp.generated.resources.line_graph
import wialonlocal.composeapp.generated.resources.max_speed


import kotlinx.datetime.*

fun getStartAndEndOfDayTimestamps(date: LocalDate): Pair<Long, Long> {
    // Set timezone to Ashgabat, Turkmenistan
    val timeZone = TimeZone.of("Asia/Ashgabat")

    // Start of the day at 00:00:00
    val startOfDay = date.atStartOfDayIn(timeZone)
    val startTimestamp = startOfDay.epochSeconds

    // End of the day at 23:59:59
    val endOfDay = date.atTime(23, 59, 59).toInstant(timeZone)
    val endTimestamp = endOfDay.epochSeconds

    return Pair(startTimestamp, endTimestamp)
}


@Composable
fun HistoryScreen(
    modifier: Modifier = Modifier,
    onRefresh: ()-> Unit = {},
    onPolylineChange: (List<String?>?) -> Unit,
    onSingleMarker: (LatLong?) -> Unit,
    onDateChanges: (Long, Long) -> Unit,
) {
    val navigator = LocalNavigator.currentOrThrow
    val viewModel: MonitoringViewModel = navigator.koinNavigatorScreenModel()
    val eventState = viewModel.loadEventState.collectAsState()
    val isFull = rememberSaveable {
        mutableStateOf(false)
    }
    val selectedDate = rememberSaveable {
        mutableStateOf(
            getStartAndEndOfDayTimestamps(
                Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
            ).first
        )
    }


    fun generateDateList(): List<LocalDate> {
        val currentDate = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
        val startDate = currentDate.minus(1, DateTimeUnit.YEAR)

        return generateSequence(startDate) { it.plus(1, DateTimeUnit.DAY) }
            .takeWhile { it <= currentDate }
            .toList()
    }


    val dates = remember {
        mutableStateOf(generateDateList())
    }

    val listState = rememberLazyListState()

    LaunchedEffect(dates.value) {
        listState.scrollToItem(dates.value.size - 1)
    }

    val summary = viewModel.summaryState.collectAsState()


    LaunchedEffect(eventState.value.data) {
        eventState.value.data?.second?.let { list ->
            if(isFull.value) {
                onPolylineChange(list.filter { it.type == "trip" }.map { it.track })
                onSingleMarker(null)
                isFull.value = true
            }
        }
    }

    Column(
        modifier = modifier.fillMaxWidth().verticalScroll(rememberScrollState()).background(
            color = MaterialTheme.colorScheme.background
        )
    ) {
        Spacer(Modifier.height(4.dp))
        LazyRow(
            state = listState,
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(
                horizontal = 16.dp
            )
        ) {
            items(dates.value.count()) { index ->
                val date = dates.value[index]
                val dayOfWeek = date.dayOfWeek.name.lowercase().replaceFirstChar { it.uppercase() }
                val month = date.month.name.lowercase().take(3).replaceFirstChar { it.uppercase() }
                val dayOfMonth = date.dayOfMonth
                HistoryDateButton(
                    dayOfWeek = dayOfWeek,
                    day = dayOfMonth.toString(),
                    month = month,
                    selected = selectedDate.value == getStartAndEndOfDayTimestamps(date).first,
                    onClick = {
                        val (startTimestamp, endTimestamp) = getStartAndEndOfDayTimestamps(date)
                        onDateChanges(startTimestamp, endTimestamp)
                        selectedDate.value = startTimestamp
                        if(isFull.value.not()) {
                            onPolylineChange(null)
                            onSingleMarker(null)
                        }
                    }
                )
            }
        }
        Spacer(Modifier.height(4.dp))

        HorizontalDivider()

        Row(
            modifier = Modifier.fillMaxWidth().clickable {
                eventState.value.data?.second?.let { list ->
                    onPolylineChange(list.filter { it.type == "trip" }.map { it.track })
                    onSingleMarker(null)
                    isFull.value = true

                }

            }.padding(12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            HistoryStatLabel(
                modifier = Modifier.weight(1f),
                icon = painterResource(Res.drawable.circle_car),
                text = summary.value.tripMin
            )
            HistoryStatLabel(
                modifier = Modifier.weight(1f),
                icon = painterResource(Res.drawable.circle_graph),
                text = summary.value.dayKm
            )
            HistoryStatLabel(
                modifier = Modifier.weight(1f),
                icon = painterResource(Res.drawable.circle_parking),
                text = summary.value.parkingMin
            )
        }
        HorizontalDivider()
        if (eventState.value.loading) {
            LinearProgressIndicator(Modifier.fillMaxWidth())
        } else if (eventState.value.error.isNullOrEmpty().not()) {
            Column(
                Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("${eventState.value.error}")
                Button(
                    onClick = {
                        onRefresh()
                    }
                ) {
                    Text("Refresh")
                }
            }
        } else {
            eventState.value.data?.second?.let { list ->
                repeat(list.count()) { index ->
                    val item = list[index]
                    when (item.type) {
                        "park" -> ParkingItem(Modifier.fillMaxWidth().clickable {
                            onPolylineChange(null)
                            isFull.value = false
                            onSingleMarker(
                                LatLong(item.to.y, item.to.x)
                            )
                        }, item)

                        "stop" -> ParkingItem(Modifier.fillMaxWidth().clickable {
                            onPolylineChange(null)
                            isFull.value = false
                            onSingleMarker(
                                LatLong(item.to.y, item.to.x)
                            )
                        }, item)

                        "trip" -> CarItem(Modifier.fillMaxWidth().clickable {
                            onPolylineChange(
                                listOf(item.track)
                            )
                            onSingleMarker(null)
                            isFull.value = false
                        }, item)
                    }
                }
            }
        }
    }
}

@Composable
fun ParkingItem(
    modifier: Modifier = Modifier,
    item: Trip
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(
                color = MaterialTheme.colorScheme.surface
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier.width(72.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                item.formatedTime, style = MaterialTheme.typography.bodyLarge.copy(
                    fontWeight = FontWeight.W500
                ),
                color = MaterialTheme.colorScheme.onBackground
            )
            Text(
                "AM", style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onBackground
            )
        }

        Box(
            modifier = Modifier
                .weight(1f)
                .border(
                    width = 0.5.dp,
                    color = MaterialTheme.colorScheme.inverseSurface
                )
                .padding(12.dp)
        ) {
            Image(
                painter = painterResource(Res.drawable.circle_parking),
                contentDescription = "parking",
                modifier = Modifier.size(30.dp).absoluteOffset(x = (-27).dp, y = 12.dp).zIndex(3f)
                    .background(
                        color = MaterialTheme.colorScheme.surface,
                        shape = CircleShape
                    )
            )

            Column(
                modifier = Modifier.fillMaxWidth().padding(start = 10.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Row(
                        modifier = Modifier.height(30.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            painter = painterResource(Res.drawable.clock),
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary
                        )
                        Spacer(Modifier.width(6.dp))
                        Text(
                            text = item.formatedDuration,
                            color = MaterialTheme.colorScheme.onSurface,
                            style = MaterialTheme.typography.bodySmall.copy(
                                fontWeight = FontWeight.W500
                            )
                        )
                    }

                    Row(
                        modifier = Modifier.height(30.dp).padding(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            painter = painterResource(Res.drawable.key),
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary
                        )
                        Spacer(Modifier.width(6.dp))
                        Text(
                            text = "5 min",
                            color = MaterialTheme.colorScheme.onSurface,
                            style = MaterialTheme.typography.bodySmall.copy(
                                fontWeight = FontWeight.W500
                            )
                        )
                    }
                }
                Text(
                    text = item.address,
                    color = MaterialTheme.colorScheme.outline,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}

@Composable
fun CarItem(
    modifier: Modifier = Modifier,
    item: Trip
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(
                color = MaterialTheme.colorScheme.secondary.copy(
                    alpha = 0.1f
                )
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier.width(72.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                item.formatedTime, style = MaterialTheme.typography.bodyLarge.copy(
                    fontWeight = FontWeight.W500
                ),
                color = MaterialTheme.colorScheme.onBackground
            )
            Text(
                "AM", style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onBackground
            )
        }

        Box(
            modifier = Modifier
                .weight(1f)
                .border(
                    width = 0.5.dp,
                    color = MaterialTheme.colorScheme.inverseSurface
                )
                .padding(12.dp)
        ) {
            Image(
                painter = painterResource(Res.drawable.circle_car),
                contentDescription = "car",
                modifier = Modifier.size(30.dp).absoluteOffset(x = (-27).dp).zIndex(3f)
            )

            Column(
                modifier = Modifier.fillMaxWidth().padding(start = 10.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Row(
                        modifier = Modifier.height(30.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            painter = painterResource(Res.drawable.clock),
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary
                        )
                        Spacer(Modifier.width(6.dp))
                        Text(
                            text = item.formatedDuration,
                            color = MaterialTheme.colorScheme.onSurface,
                            style = MaterialTheme.typography.bodySmall.copy(
                                fontWeight = FontWeight.W500
                            )
                        )
                    }

                    Row(
                        modifier = Modifier.height(30.dp).padding(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            painter = painterResource(Res.drawable.line_graph),
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary
                        )
                        Spacer(Modifier.width(6.dp))
                        Text(
                            text = item.format.distance,
                            color = MaterialTheme.colorScheme.onSurface,
                            style = MaterialTheme.typography.bodySmall.copy(
                                fontWeight = FontWeight.W500
                            )
                        )
                    }

                    Row(
                        modifier = Modifier.height(30.dp).padding(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            painter = painterResource(Res.drawable.max_speed),
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary
                        )
                        Spacer(Modifier.width(6.dp))
                        Text(
                            text = "${item.max_speed} km",
                            color = MaterialTheme.colorScheme.onSurface,
                            style = MaterialTheme.typography.bodySmall.copy(
                                fontWeight = FontWeight.W500
                            )
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun HistoryStatLabel(
    modifier: Modifier = Modifier,
    icon: Painter,
    text: String
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Image(
            painter = icon,
            contentDescription = text,
            modifier = Modifier.size(30.dp)
        )
        Text(
            text = text,
            color = MaterialTheme.colorScheme.onBackground,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@Composable
fun HistoryDateButton(
    modifier: Modifier = Modifier,
    dayOfWeek: String,
    day: String,
    month: String,
    selected: Boolean = false,
    onClick: () -> Unit
) {
    val shape = RoundedCornerShape(20.dp)
    Column(
        modifier = modifier
            .clip(shape)
            .background(
                color = if (selected) MaterialTheme.colorScheme.primary else Color.Transparent,
                shape = shape
            )
            .border(
                width = if (selected) 0.dp else 1.dp,
                shape = shape,
                color = MaterialTheme.colorScheme.inverseSurface
            )
            .clickable {
                onClick()
            }
            .padding(horizontal = 18.dp, vertical = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            dayOfWeek.take(3),
            color = if (selected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant,
            style = MaterialTheme.typography.bodySmall
        )

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                day,
                color = if (selected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onBackground,
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.W500
                )
            )
            Text(
                month,
                color = if (selected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onBackground,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}