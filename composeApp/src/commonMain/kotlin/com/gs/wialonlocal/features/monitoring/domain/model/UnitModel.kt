package com.gs.wialonlocal.features.monitoring.domain.model

import androidx.compose.ui.graphics.Color
import com.gs.wialonlocal.common.LatLong
import dev.icerock.moko.parcelize.Parcelable
import dev.icerock.moko.parcelize.Parcelize
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import kotlinx.serialization.Serializable

fun getCurrentTimeInEpochSeconds(): Long {
    // Get the current moment
    val now = Clock.System.now()

    // Convert to Asia/Ashgabat timezone
    val ashgabatTimeZone = TimeZone.of("Asia/Ashgabat")
    val localDateTime = now.toLocalDateTime(ashgabatTimeZone)

    // Convert back to Instant and get EpochSeconds
    val instant = localDateTime.toInstant(ashgabatTimeZone)
    return instant.epochSeconds
}

@Parcelize
@Serializable
data class UnitModel(
    val id: String,
    val carNumber: String,
    val number: String,
    val image: String,
    val lastOnlineTime: String,
    val address: String,
    val speed: String,
    val estimateTime: String,
    val estimateDistance: String,
    val isOnline: Boolean,
    var latitude: Double,
    var longitude: Double,
    var trips: List<LatLong> = emptyList(),
    var moving: Boolean = false,
    var stationary: Boolean = false,
    var stationaryWithIgnitionOn: Boolean = false,
    var ignitionOn: Boolean = false,
    var noActualState: Boolean = false,
    var noMessages: Boolean = true
): Parcelable {

    fun calculateDifference(): Pair<String, Color> {
        // Get the current date in the Asia/Ashgabat timezone
        val timeZone = TimeZone.of("Asia/Ashgabat")
        val currentDate = Clock.System.now().toLocalDateTime(timeZone)

        // Convert lastOnlineTime (assuming it's a timestamp in seconds) to Instant and adjust to the same timezone
        val pastDate = try {
            Instant.fromEpochSeconds(lastOnlineTime.toLong()).toLocalDateTime(timeZone)
        } catch (ex: Exception) {
            Clock.System.now().toLocalDateTime(timeZone)
        }

        // Calculate the duration between currentDate and pastDate
        val duration = currentDate.toInstant(timeZone) - pastDate.toInstant(timeZone)

        // Optionally, convert this duration to a more readable format
        val days = duration.inWholeDays
        val hours = (duration.inWholeHours % 24).toInt()
        val minutes = (duration.inWholeMinutes % 60).toInt()
        val seconds = (duration.inWholeSeconds % 60).toInt()

        // Return the appropriate time difference and color based on duration
        return if (days >= 1) {
            Pair("$days days", Color(0xFFDEDEDE))
        } else if (hours >= 1) {
            Pair("$hours h", Color(0xFFFBE0D7))
        } else if (minutes > 5) {
            Pair("$minutes min", Color(0xFFFEF7DB))
        } else if (minutes > 0) {
            Pair("$minutes min", Color(0xFFDFEEDD))
        } else {
            Pair("$seconds s", Color(0xFFDFEEDD))
        }
    }

    fun convertTime(): String {
        val timeZone = TimeZone.of("Asia/Ashgabat")
        val currentDate = Clock.System.now().toLocalDateTime(timeZone)

        // Convert lastOnlineTime (assuming it's a timestamp in seconds) to Instant and adjust to the same timezone
        val pastDate = try {
            Instant.fromEpochSeconds(estimateTime.toLong()).toLocalDateTime(timeZone)
        } catch (ex: Exception) {
            Clock.System.now().toLocalDateTime(timeZone)
        }

        // Calculate the duration between currentDate and pastDate
        val duration = currentDate.toInstant(timeZone) - pastDate.toInstant(timeZone)

        // Optionally, convert this duration to a more readable format
        val days = duration.inWholeDays
        val hours = (duration.inWholeHours % 24).toInt()
        val minutes = (duration.inWholeMinutes % 60).toInt()
        val seconds = (duration.inWholeSeconds % 60).toInt()

        var result = ""

        if(days>0){
            result = result.plus("$days d")
        } else if(hours>0){
            result = result.plus("$hours h")
        } else if(minutes>0){
            result = result.plus("$minutes min")
        } else if(seconds>0) {
            result = result.plus("$seconds s")
        }

        return result
    }
}
