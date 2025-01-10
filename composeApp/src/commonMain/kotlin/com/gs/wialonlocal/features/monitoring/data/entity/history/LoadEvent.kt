package com.gs.wialonlocal.features.monitoring.data.entity.history

import com.gs.wialonlocal.features.monitoring.data.entity.TripDetector
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.decodeFromJsonElement
import kotlin.time.Duration
import kotlinx.datetime.*
import kotlin.math.floor

@Serializable
data class TripEvent(
    val trips: Map<String, Int>
)

@Serializable
data class GetEvents(
    val trips: List<Trip>? = null
)

@Serializable
data class TripState(
    val trips: TripStateData
)

@Serializable
data class TripStateData(
    val updateTime: Long
)

@Serializable
data class TripSelector(
    val trips: Map<String, List<Trip>>
)

@Serializable
data class Trip(
    val from: TripPoint,
    val to: TripPoint,
    val m: Long,
    val f: Int,
    val state: Int,
    val max_speed: Int,
    val curr_speed: Int,
    val avg_speed: Int,
    val distance: Int,
    val odometer: Long,
    val course: Int,
    val altitude: Int,
    val pos_flags: Int,
    val format: TripFormat,
    val track: String,
    val p: JsonObject,
    var type: String = "", // New field to store the type (trip, stop, park),
    var formatedTime: String="",
    var formatedDuration: String="",
    val address: String = ""
)


@Serializable
data class TripPoint(
    val t: Long,
    val y: Double,
    val x: Double
)

@Serializable
data class TripFormat(
    val distance: String,
    val avg_speed: String
)

@Serializable
data class TripsResponse(
    val events: TripEvent,
    val states: TripState,
    val selector: TripSelector
)


fun formatTime(minutes: Long): String {
    val hours = minutes / 60
    val remainingMinutes = minutes % 60

    return when {
        hours > 0 -> "$hours h $remainingMinutes min"
        else -> "$remainingMinutes min"
    }
}

fun formatDistance(distanceInMeters: Double): String {
    return when {
        distanceInMeters >= 1000 -> {
            val km = distanceInMeters / 1000
            "${floor(km * 100) / 100} km"  // Round to 2 decimal places
        }
        else -> "${floor(distanceInMeters)} metr"  // No decimals for meters
    }
}

fun calculateTripStats(trips: List<Trip>): Pair<String, String> {
    var totalTripTimeInSeconds: Long = 0
    var totalDistanceInMeters: Double = 0.0

    for (trip in trips) {
        // Calculate trip duration in seconds
        val tripDuration = trip.to.t - trip.from.t
        totalTripTimeInSeconds += tripDuration

        // Calculate total distance in meters
        totalDistanceInMeters += trip.distance.toDouble()
    }

    // Convert total trip time from seconds to minutes
    val totalTripTimeInMinutes = totalTripTimeInSeconds / 60

    // Format time and distance
    val formattedTime = formatTime(totalTripTimeInMinutes)
    val formattedDistance = formatDistance(totalDistanceInMeters)

    return Pair(formattedTime, formattedDistance)
}

fun convertMinutesToHours(minutes: Long): String {
    if(minutes>=60){
        val hours = (minutes / 60).toInt()
        val minute = (minutes % 60)
        return "$hours h  $minute min"
    }
    return minutes.toString().plus(" min")
}


fun categorizeAndMergeTrips(tripsResponse: List<Trip>?, tripDetector: TripDetector): List<Trip> {
    val allTrips = mutableListOf<Trip>()


    tripsResponse?.let { list->
        list.forEachIndexed { index, trip ->
            val (tripDate, tripStartTime) = convertTimestampToDateTime(trip.from.t)
            val (_, tripEndTime) = convertTimestampToDateTime(trip.to.t)
            trip.type = "trip"
            val tripDurationMinutes = (trip.to.t - trip.from.t) / 60
            trip.formatedDuration = convertMinutesToHours(tripDurationMinutes)
            trip.formatedTime = tripStartTime
            allTrips.add(trip)

            val nextTrip = try {
                list[index+1]
            } catch (ex: Exception) {
                null
            }

            if(nextTrip!=null){
                val diff = nextTrip.from.t - trip.to.t
                val parkingDurationMinutes = (nextTrip.from.t - trip.to.t) / 60
                val (parkingDate, parkingStartTime) = convertTimestampToDateTime(trip.to.t)
                val parkingTrip = Trip(
                    from = trip.to,
                    to = nextTrip.from,
                    type = "park",
                    state = 0,
                    f = trip.f,
                    m = diff,
                    p = trip.p,
                    track = trip.track,
                    altitude = trip.altitude,
                    distance = 0,
                    odometer = trip.odometer,
                    course = 0,
                    format = TripFormat(
                        distance = "0 km",
                        avg_speed = "0 km/h"
                    ),
                    avg_speed = trip.avg_speed,
                    max_speed = trip.max_speed,
                    pos_flags = trip.pos_flags,
                    curr_speed = trip.curr_speed,
                    formatedDuration = convertMinutesToHours(parkingDurationMinutes),
                    formatedTime = parkingStartTime,
                    address = ""
                )

                allTrips.add(parkingTrip)
            }

        }
    }




    // Sort by the 'from' time (trip starting time)
    return allTrips
}

fun convertTimestampToDateTime(timestamp: Long): Pair<String, String> {
    val instant = Instant.fromEpochSeconds(timestamp)
    val dateTime = instant.toLocalDateTime(TimeZone.of("Asia/Ashgabat"))

    val day = dateTime.dayOfMonth.toString().padStart(2, '0')
    val month = dateTime.monthNumber.toString().padStart(2, '0')
    val year = dateTime.year
    val hour = dateTime.hour.toString().padStart(2, '0')
    val minute = dateTime.minute.toString().padStart(2, '0')

    return Pair("$day.$month.$year", "$hour:$minute")
}


/*
TripDetector(
gpsCorrection=1,
maxMessagesDistance=10000.0,
minMovingSpeed=2.0,
minSat=2.0,
minStayTime=300.0,
minTripDistance=100.0,
minTripTime=60.0,
type=1)
 */