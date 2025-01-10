package com.gs.wialonlocal.features.report.presentation.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.lyricist.strings
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.koinNavigatorScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.gs.wialonlocal.features.main.presentation.ui.ToolBar
import com.gs.wialonlocal.features.monitoring.presentation.viewmodel.MonitoringViewModel
import com.gs.wialonlocal.features.report.presentation.viewmodel.ReportViewModel
import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import kotlinx.datetime.atTime
import kotlinx.datetime.minus
import kotlinx.datetime.plus
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import org.jetbrains.compose.resources.painterResource
import wialonlocal.composeapp.generated.resources.Res
import wialonlocal.composeapp.generated.resources.screen_rotation
import wialonlocal.composeapp.generated.resources.template
import wialonlocal.composeapp.generated.resources.unit

fun formatUnixTimestampToDateTime(unixTimestamp: Long, timeZone: TimeZone): String {
    // Convert Unix timestamp (seconds) to milliseconds
    val millis = unixTimestamp * 1000

    // Create an Instant from the Unix timestamp in milliseconds
    val instant = Instant.fromEpochMilliseconds(millis)

    // Convert the Instant to a LocalDateTime in the given time zone
    val localDateTime = instant.toLocalDateTime(timeZone)

    // Extract the date and time components
    val day = localDateTime.dayOfMonth.toString().padStart(2, '0')
    val month = localDateTime.monthNumber.toString().padStart(2, '0')
    val year = localDateTime.year
    val hour = localDateTime.hour.toString().padStart(2, '0')
    val minute = localDateTime.minute.toString().padStart(2, '0')

    // Format the date and time as "dd.MM.yyyy | HH:mm"
    return "$day.$month.$year | $hour:$minute"
}

fun getStartAndEndMillis(): Pair<Long, Long> {
    // Get the current moment in UTC
    val currentMoment = Clock.System.now()

    // Convert to local date-time based on the system's current time zone
    val timeZone = TimeZone.of("Asia/Ashgabat")
    val today = currentMoment.toLocalDateTime(timeZone).date

    // Get the start of today
//    val startOfToday = today.atStartOfDayIn(timeZone)

    // Get the start of tomorrow by adding one day to today
    val startOfToday = today.plus(1, DateTimeUnit.DAY).atStartOfDayIn(timeZone)
    val startOfTomorrow = today.atTime(23,59).toInstant(timeZone)

    // Return the start and end times in milliseconds
    return Pair(startOfToday.toEpochMilliseconds(), startOfTomorrow.toEpochMilliseconds())
}

fun getYesterday(): Pair<Long, Long> {
    // Get the current moment in UTC
    val currentMoment = Clock.System.now()

    // Convert to local date-time based on the system's current time zone
    val timeZone = TimeZone.of("Asia/Ashgabat")
    val today = currentMoment.toLocalDateTime(timeZone).date

    // Get the start of today
//    val startOfToday = today.atStartOfDayIn(timeZone)

    // Get the start of tomorrow by adding one day to today
    val startOfToday = today.atStartOfDayIn(timeZone)
    val startOfTomorrow = today.minus(1, DateTimeUnit.DAY).atTime(23,59).toInstant(timeZone)

    // Return the start and end times in milliseconds
    return Pair(startOfToday.toEpochMilliseconds(), startOfTomorrow.toEpochMilliseconds())
}

fun getWeek(): Pair<Long, Long> {
    // Get the current moment in UTC
    val currentMoment = Clock.System.now()

    // Convert to local date-time based on the system's current time zone
    val timeZone = TimeZone.of("Asia/Ashgabat")
    val today = currentMoment.toLocalDateTime(timeZone).date

    // Get the start of today
//    val startOfToday = today.atStartOfDayIn(timeZone)

    // Get the start of tomorrow by adding one day to today
    val startOfToday = today.minus(1, DateTimeUnit.WEEK).plus(1, DateTimeUnit.DAY).atStartOfDayIn(timeZone)
    val startOfTomorrow = today.atTime(23,59).toInstant(timeZone)

    // Return the start and end times in milliseconds
    return Pair(startOfToday.toEpochMilliseconds(), startOfTomorrow.toEpochMilliseconds())
}

fun getMonth(): Pair<Long, Long> {
    // Get the current moment in UTC
    val currentMoment = Clock.System.now()

    // Convert to local date-time based on the system's current time zone
    val timeZone = TimeZone.of("Asia/Ashgabat")
    val today = currentMoment.toLocalDateTime(timeZone).date

    // Get the start of today
//    val startOfToday = today.atStartOfDayIn(timeZone)

    // Get the start of tomorrow by adding one day to today
    val startOfToday = today.minus(1, DateTimeUnit.MONTH).plus(1, DateTimeUnit.DAY).atStartOfDayIn(timeZone)
    val startOfTomorrow = today.atTime(23,59).toInstant(timeZone)

    // Return the start and end times in milliseconds
    return Pair(startOfToday.toEpochMilliseconds(), startOfTomorrow.toEpochMilliseconds())
}

class ReportScreen: Screen {
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow

        val viewModel = navigator.koinNavigatorScreenModel<ReportViewModel>()

        val selectedTemplate = viewModel.selectedTemplate

        val selectedUnit = viewModel.selectedUnit
        val selectedOrientation = viewModel.selectedOrientation

        val selectedStartDate = viewModel.selectedStartDate
        val selectedEndDate = viewModel.selectedEndDate

        val monitoringViewModel: MonitoringViewModel = navigator.koinNavigatorScreenModel()

        LaunchedEffect(true) {
            viewModel.initTemplates()
            monitoringViewModel.initUnits(requireCheckUpdate = false, false)
        }

        val openStart = remember {
            mutableStateOf(false)
        }

        val openOrientation = remember {
            mutableStateOf(false)
        }

        val reportState = viewModel.reportState.collectAsState()

        val startDateState = rememberDatePickerState(
            initialSelectedDateMillis = getStartAndEndMillis().first
        )
        val endDateState = rememberDatePickerState(
            initialSelectedDateMillis = getStartAndEndMillis().second
        )
        val startTimeState = rememberTimePickerState(
            initialHour = 0,
            initialMinute = 0,
            is24Hour = true
        )
        val endTimeState = rememberTimePickerState(
            initialHour = 23,
            initialMinute = 59,
            is24Hour = true
        )

        LaunchedEffect(startTimeState, startDateState.selectedDateMillis) {
            viewModel.setStartDate(convertToUnixTimestamp(startDateState.selectedDateMillis, startTimeState.hour, startTimeState.minute))
        }

        LaunchedEffect(endTimeState, endDateState.selectedDateMillis) {
            viewModel.setEndDate(convertToUnixTimestamp(endDateState.selectedDateMillis, endTimeState.hour, endTimeState.minute))
        }

        val openEnd = remember {
            mutableStateOf(false)
        }
        ReportDatePicker(
            title = "Start",
            open = openStart.value,
            onDismiss = {
                openStart.value = false
            },
            onSelect = {
                viewModel.setStartDate(convertToUnixTimestamp(startDateState.selectedDateMillis, startTimeState.hour, startTimeState.minute))
            },
            timePickerState = startTimeState,
            datePickerState = startDateState
        )

        ReportDatePicker(
            title = "End",
            open = openEnd.value,
            onDismiss = {
                openEnd.value = false
            },
            onSelect = {
                viewModel.setEndDate(convertToUnixTimestamp(endDateState.selectedDateMillis, endTimeState.hour, endTimeState.minute))
            },
            timePickerState = endTimeState,
            datePickerState = endDateState
        )

        OrientationDialog(
            open = openOrientation.value,
            onDismiss = {
                openOrientation.value = false
            }
        )
        Column(Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)) {
            ToolBar(modifier = Modifier.fillMaxWidth()) {
                Box(Modifier.padding(16.dp)) {
                    Text(
                        text = strings.reports,
                        color = MaterialTheme.colorScheme.onPrimary,
                        style = MaterialTheme.typography.bodyLarge.copy(
                            fontWeight = FontWeight.W500,
                            fontSize = 20.sp
                        )
                    )
                }
            }

            Column(Modifier.fillMaxWidth().weight(1f)) {
                ReportButton(
                    modifier = Modifier.fillMaxWidth().clickable {
                        navigator.push(TemplateSelect())
                    },
                    title = strings.template,
                    subTitle = if (selectedTemplate.value != null) selectedTemplate.value!!.name else strings.notSelected,
                    icon = painterResource(Res.drawable.template),
                    showArrow = true
                )
                ReportButton(
                    modifier = Modifier.fillMaxWidth().clickable {
                        navigator.push(ReportUnitSelect())
                    },
                    title = strings.unit,
                    subTitle = if(selectedUnit.value!=null) selectedUnit.value!!.carNumber else strings.notSelected,
                    icon = painterResource(Res.drawable.unit),
                    showArrow = true
                )

                Spacer(Modifier.height(16.dp))
                ReportDateRange(
                    modifier = Modifier.fillMaxWidth(),
                    startDate = formatSelectedDateMillis(startDateState.selectedDateMillis?:0, TimeZone.of("Asia/Ashgabat")) + " | " + formatTime(startTimeState.hour, startTimeState.minute, startTimeState.is24hour, startTimeState.isAfternoon),
                    endDate = formatSelectedDateMillis(endDateState.selectedDateMillis?:0, TimeZone.of("Asia/Ashgabat")) + " | " + formatTime(endTimeState.hour, endTimeState.minute, endTimeState.is24hour, endTimeState.isAfternoon),
                    onShortDateSelected = { index->
                        when(index) {
                            0 -> {
                                startDateState.selectedDateMillis = getStartAndEndMillis().first
                                endDateState.selectedDateMillis = getStartAndEndMillis().second
                            }
                            1 -> {
                                startDateState.selectedDateMillis = getYesterday().first
                                endDateState.selectedDateMillis = getYesterday().second
                            }
                            2 -> {
                                startDateState.selectedDateMillis = getWeek().first
                                endDateState.selectedDateMillis = getWeek().second
                            }
                            3 -> {
                                startDateState.selectedDateMillis = getMonth().first
                                endDateState.selectedDateMillis = getMonth().second
                            }
                        }
                    },
                    onStartDateClick = {
                        openStart.value = true
                    },
                    onEndDateClick = {
                        openEnd.value = true
                    }
                )
                Spacer(Modifier.height(16.dp))
                ReportButton(
                    modifier = Modifier.fillMaxWidth().clickable {
                        openOrientation.value = true
                    },
                    title = strings.orientation,
                    subTitle = selectedOrientation.value,
                    icon = painterResource(Res.drawable.screen_rotation),
                    showArrow = true
                )
            }

            Box(Modifier.padding(16.dp).fillMaxWidth()) {
                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {
                        viewModel.execute(
                            onSuccess = {
                                navigator.push(ReportView())
                            }
                        )
                    },
                    shape = RoundedCornerShape(2.dp)
                ) {
                    if(reportState.value.loading) {
                        CircularProgressIndicator(
                            color = Color.White
                        )
                    } else {
                        Text(
                            strings.executeReport,
                            color = MaterialTheme.colorScheme.onPrimary,
                            style = MaterialTheme.typography.bodyLarge.copy(
                                fontWeight = FontWeight.W500
                            )
                        )
                    }
                }
            }
        }

    }
}