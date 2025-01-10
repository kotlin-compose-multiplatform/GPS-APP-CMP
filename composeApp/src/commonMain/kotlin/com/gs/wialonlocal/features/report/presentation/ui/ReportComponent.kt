package com.gs.wialonlocal.features.report.presentation.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.DropdownMenu
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowRight
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.automirrored.filled.More
import androidx.compose.material.icons.filled.ArrowRight
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DatePickerState
import androidx.compose.material3.DateRangePicker
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TimePickerState
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberDateRangePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import cafe.adriel.lyricist.strings
import cafe.adriel.voyager.koin.koinNavigatorScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.gs.wialonlocal.features.main.presentation.ui.ToolBar
import com.gs.wialonlocal.features.report.presentation.viewmodel.ReportViewModel
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import kotlinx.datetime.toLocalDateTime
import org.jetbrains.compose.resources.painterResource
import wialonlocal.composeapp.generated.resources.Res
import wialonlocal.composeapp.generated.resources.calendar
import wialonlocal.composeapp.generated.resources.template
import kotlin.time.DurationUnit
import kotlin.time.toDuration

@Composable
fun ReportButton(
    modifier: Modifier = Modifier,
    icon: Painter,
    title: String,
    subTitle: String,
    showArrow: Boolean
) {
    Column(modifier.background(MaterialTheme.colorScheme.surface)) {
        Row(
            Modifier.fillMaxWidth().padding(vertical = 10.dp, horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Icon(
                painter = icon,
                contentDescription = title,
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(24.dp)
            )

            Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(2.dp)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Text(
                    text = subTitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            if (showArrow) {
                Icon(
                    Icons.AutoMirrored.Default.KeyboardArrowRight,
                    contentDescription = "right",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
        HorizontalDivider(
            color = MaterialTheme.colorScheme.inverseSurface
        )
    }
}

@Composable
fun ReportDateRange(
    modifier: Modifier = Modifier,
    startDate: String,
    endDate: String,
    onShortDateSelected: (Int) -> Unit,
    onStartDateClick: () -> Unit,
    onEndDateClick: () -> Unit,
) {
    val open = remember {
        mutableStateOf(false)
    }

    Column(modifier.background(MaterialTheme.colorScheme.surface)) {
        Row(
            Modifier.fillMaxWidth().padding(vertical = 10.dp, horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Icon(
                painter = painterResource(Res.drawable.calendar),
                contentDescription = "calendar",
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(24.dp)
            )

            Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(2.dp)) {
                Text(
                    text = strings.interval,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }
            IconButton(
                onClick = {
                    open.value = true
                }
            ) {
                Icon(
                    Icons.Default.MoreVert,
                    contentDescription = "more",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )

                ReportShortDate(
                    open = open.value,
                    onDismiss = { index->
                        if(index!=-1) {
                            onShortDateSelected(index)
                        }
                        open.value = false
                    }
                )
            }


        }
        Row(
            Modifier.clickable {
                onStartDateClick()
            }.padding(horizontal = 50.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Text(
                text = startDate,
                color = MaterialTheme.colorScheme.onBackground,
                style = MaterialTheme.typography.bodyLarge
            )
        }

        Spacer(Modifier.height(12.dp))

        Row(
            Modifier.clickable {
                onEndDateClick()
            }.padding(horizontal = 50.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Text(
                text = endDate,
                color = MaterialTheme.colorScheme.onBackground,
                style = MaterialTheme.typography.bodyLarge
            )
        }
        Spacer(Modifier.height(12.dp))
        HorizontalDivider(
            color = MaterialTheme.colorScheme.inverseSurface
        )
    }
}

@Composable
fun ReportShortDate(modifier: Modifier = Modifier, open: Boolean, onDismiss: (Int) -> Unit) {
    DropdownMenu(
        modifier = modifier.background(
            color = MaterialTheme.colorScheme.surface
        ),
        expanded = open,
        onDismissRequest = {
            onDismiss(-1)
        }
    ) {
        DropdownMenuItem(
            text = {
                Text(
                    text = strings.today,
                    color = MaterialTheme.colorScheme.onSurface,
                    style = MaterialTheme.typography.bodyMedium
                )
            },
            onClick = {
                onDismiss(0)
            }
        )
        DropdownMenuItem(
            text = {
                Text(
                    text = strings.yesterday,
                    color = MaterialTheme.colorScheme.onSurface,
                    style = MaterialTheme.typography.bodyMedium
                )
            },
            onClick = {
                onDismiss(1)
            }
        )
        DropdownMenuItem(
            text = {
                Text(
                    text = strings.week,
                    color = MaterialTheme.colorScheme.onSurface,
                    style = MaterialTheme.typography.bodyMedium
                )
            },
            onClick = {
                onDismiss(2)
            }
        )

        DropdownMenuItem(
            text = {
                Text(
                    text = strings.month,
                    color = MaterialTheme.colorScheme.onSurface,
                    style = MaterialTheme.typography.bodyMedium
                )
            },
            onClick = {
                onDismiss(3)
            }
        )
    }
}

fun convertFromUnixTimestamp(unixTimestamp: Long, timeZone: TimeZone): Triple<Long, Int, Int> {
    // Convert Unix timestamp (seconds) to milliseconds
    val millis = unixTimestamp * 1000

    // Create an Instant from the Unix timestamp in milliseconds
    val instant = Instant.fromEpochMilliseconds(millis)

    // Convert the instant to a LocalDateTime in the specified time zone
    val localDateTime = instant.toLocalDateTime(timeZone)

    // Extract the date in milliseconds (representing midnight of that date)
    val selectedDateMillis = localDateTime.date.atStartOfDayIn(timeZone).toEpochMilliseconds()

    // Extract the hour and minute
    val hour = localDateTime.hour
    val minute = localDateTime.minute

    // Return the selectedDateMillis, hour, and minute as a Triple
    return Triple(selectedDateMillis, hour, minute)
}

fun convertToUnixTimestamp(selectedDateMillis: Long?, hour: Int, minute: Int): Long? {
    if (selectedDateMillis == null) {
        return null
    }

    // Add the hour and minute to the selected date in milliseconds
    val totalMillis = selectedDateMillis +
            hour.toDuration(DurationUnit.HOURS).inWholeMilliseconds +
            minute.toDuration(DurationUnit.MINUTES).inWholeMilliseconds

    // Convert to Unix timestamp by dividing by 1000 (milliseconds to seconds)
    return totalMillis / 1000
}

fun formatSelectedDateMillis(selectedDateMillis: Long, timeZone: TimeZone): String {
    // Convert the milliseconds to an Instant
    val instant = Instant.fromEpochMilliseconds(selectedDateMillis)

    // Convert the Instant to a LocalDate in the given time zone
    val localDate = instant.toLocalDateTime(timeZone).date

    // Format the date as "dd.MM.yyyy"
    return "${localDate.dayOfMonth.toString().padStart(2, '0')}." +
            "${localDate.monthNumber.toString().padStart(2, '0')}." +
            "${localDate.year}"
}

fun formatTime(hour: Int, minute: Int, is24hour: Boolean, isAfternoon: Boolean): String {
    val formattedMinute = minute.toString().padStart(2, '0')

    return if (is24hour) {
        // Format time in 24-hour format
        "${hour.toString().padStart(2, '0')}:$formattedMinute"
    } else {
        // Format time in 12-hour format
        val amPm = if (isAfternoon) "PM" else "AM"
        val formattedHour = when {
            hour == 0 -> 12 // 12 AM case
            hour > 12 -> (hour - 12).toString().padStart(2, '0') // Convert to 12-hour format
            else -> hour.toString().padStart(2, '0') // Keep the hour as is
        }
        "$formattedHour:$formattedMinute $amPm"
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReportDatePicker(
    title: String,
    open: Boolean,
    timePickerState: TimePickerState,
    datePickerState: DatePickerState,
    onSelect: () -> Unit = {},
    onDismiss: () -> Unit
) {


    if (open) {

        val isTime = remember {
            mutableStateOf(false)
        }




        Dialog(
            properties = DialogProperties(
                usePlatformDefaultWidth = false
            ),
            onDismissRequest = onDismiss
        ) {
            Column(Modifier.fillMaxWidth().background(MaterialTheme.colorScheme.background)) {
                ToolBar {
                    Row(Modifier.padding(12.dp)) {
                        Text(
                            title,
                            color = MaterialTheme.colorScheme.onPrimary,
                            style = MaterialTheme.typography.bodyLarge.copy(
                                fontWeight = FontWeight.W500
                            )
                        )
                    }
                }
                if(isTime.value) {
                    Box(Modifier.weight(1f).fillMaxWidth(), contentAlignment = Alignment.Center) {
                        TimePicker(
                            state = timePickerState
                        )
                    }
                } else {
                    DatePicker(
                        modifier = Modifier.weight(1f),
                        state = datePickerState
                    )
                }
                HorizontalDivider()
                Row(
                    Modifier.fillMaxWidth().background(MaterialTheme.colorScheme.surface)
                        .padding(12.dp), verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        modifier = Modifier.clickable {
                            isTime.value = isTime.value.not()
                        },
                        text = if(isTime.value.not()) formatTime(timePickerState.hour, timePickerState.minute, timePickerState.is24hour, timePickerState.isAfternoon) else formatSelectedDateMillis(datePickerState.selectedDateMillis?:0, TimeZone.of("Asia/Ashgabat")),
                        style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.W500),
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        modifier = Modifier.clickable {
                            onSelect()
                            onDismiss()
                        },
                        text = "OK",
                        style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.W500),
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
    }
}

@Composable
fun OrientationDialog(
    open: Boolean,
    onDismiss: () -> Unit
) {
    val navigator = LocalNavigator.currentOrThrow
    val viewModel = navigator.koinNavigatorScreenModel<ReportViewModel>()
    val orientation = viewModel.selectedOrientation
    if(open) {
        Dialog(
            onDismissRequest = onDismiss
        ) {
            Column(Modifier.background(MaterialTheme.colorScheme.surface)) {
                Spacer(Modifier.height(16.dp))
                Text(
                    strings.orientation,
                    modifier = Modifier.padding(start = 16.dp),
                    color = MaterialTheme.colorScheme.onBackground,
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontWeight = FontWeight.W500
                    )
                )
                Row(Modifier.fillMaxWidth().clickable {
                    viewModel.setOrientation("landscape")
                    onDismiss()
                }, verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    RadioButton(
                        selected = orientation.value == "landscape",
                        onClick = {
                            viewModel.setOrientation("landscape")
                            onDismiss()
                        }
                    )

                    Text(
                        strings.landscape,
                        color = MaterialTheme.colorScheme.onBackground,
                        style = MaterialTheme.typography.bodyLarge.copy(
                            fontWeight = FontWeight.W500
                        )
                    )
                }
                Row(Modifier.fillMaxWidth().clickable {
                    viewModel.setOrientation("portrait")
                    onDismiss()
                }, verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    RadioButton(
                        selected = orientation.value == "portrait",
                        onClick = {
                            viewModel.setOrientation("portrait")
                            onDismiss()
                        }
                    )

                    Text(
                        strings.portrail,
                        color = MaterialTheme.colorScheme.onBackground,
                        style = MaterialTheme.typography.bodyLarge.copy(
                            fontWeight = FontWeight.W500
                        )
                    )
                }
                Spacer(Modifier.height(6.dp))
                HorizontalDivider()
                Spacer(Modifier.height(6.dp))
                Row(
                    Modifier.fillMaxWidth().background(MaterialTheme.colorScheme.surface)
                        .padding(12.dp), verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.End
                ) {

                    Text(
                        modifier = Modifier.clickable {
                            onDismiss()
                        },
                        text = strings.cancel,
                        style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.W500),
                        color = MaterialTheme.colorScheme.primary
                    )

                    Spacer(Modifier.width(12.dp))

                    Text(
                        modifier = Modifier.clickable {
                            onDismiss()
                        },
                        text = "OK",
                        style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.W500),
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
    }
}