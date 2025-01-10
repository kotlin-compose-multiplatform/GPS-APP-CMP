package com.gs.wialonlocal.core.date

import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant

fun convertToUnixTimestamp(year: Int, month: Int, day: Int, hour: Int, minute: Int, second: Int): Long {
    val dateTime = LocalDateTime(year, month, day, hour, minute, second)
    val instant = dateTime.toInstant(TimeZone.UTC)
    return instant.epochSeconds
}