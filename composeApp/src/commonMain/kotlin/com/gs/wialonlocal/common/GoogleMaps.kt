package com.gs.wialonlocal.common

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import com.gs.wialonlocal.features.geofence.data.entity.geofence.P
import com.gs.wialonlocal.features.monitoring.data.entity.history.Trip
import com.gs.wialonlocal.features.monitoring.domain.model.UnitModel
import com.gs.wialonlocal.features.settings.data.settings.MapType
import dev.icerock.moko.parcelize.Parcelable
import dev.icerock.moko.parcelize.Parcelize
import kotlinx.serialization.Serializable
import kotlin.math.PI

fun Double.metersToMiles(): Double {
    return this * 0.000621371
}
fun Double.milesToMeters(): Double {
    return this * 1609.34
}
fun Double.milesToKilometers(): Double {
    return this * 1.60934
}

fun Double.kilometersToMiles(): Double {
    return this * 0.621371
}

fun Double.metersToDegrees(latitude: Double): Double {
    val earthRadius = 3960.0 // in miles
    val radiansToDegrees = 180.0 / PI
    return (this / earthRadius) * radiansToDegrees
}

@Parcelize
@Serializable
data class LatLong(val latitude: Double = 0.0, val longitude: Double = 0.0): Parcelable

data class LatLongZoom(
    val latLong: LatLong,
    val zoom: Float
)

typealias MarkerIdStr = String // e.g. "M2580", always starts with "M"

@Serializable
data class Marker(
    // Basic info (from marker index html page)
    val id: MarkerIdStr = "",
    val position: LatLong = LatLong(0.0, 0.0),
    val title: String = "",
    val subtitle: String = "",
    val alpha: Float = 1.0f,

    // For Map/Speaking
    val isSeen: Boolean = false, // Has been within the talkRadius of the user
    val isSpoken: Boolean = false, // Has been spoken by the user or automatically by the app
    val isAnnounced: Boolean = false, // Has been announced automatically by the app (title only)

    // Marker Details (from from marker details html page
    val isDetailsLoaded: Boolean = false,
    val markerDetailsPageUrl: String = "",
    val mainPhotoUrl: String = "",
    val markerPhotos: List<String> = listOf(),
    val photoCaptions: List<String> = listOf(),
    val photoAttributions: List<String> = listOf(),
    val inscription: String = "",
    val englishInscription: String = "",
    val spanishInscription: String = "",
    val erected: String = "",
    val credits: String = "",
    val location: String = "",

    // LEAVE FOR FUTURE REFACTOR
    val markerPhotos2: List<MarkerPhoto> = listOf(), // todo consolidate with markerPhotos, photoCaptions, photoAttributions
    val lastUpdatedDetailsEpochSeconds: Long = 0, // for cache expiry
)

@Serializable
data class MarkerPhoto(
    val imageUrl: String = "",
    val caption: String = "",
    val attribution: String = ""
)

class CameraPosition(
    val target: LatLong = LatLong(0.0, 0.0),
    val zoom: Float = 0f
)

class CameraLocationBounds(
    val coordinates: List<LatLong> = listOf(),
    val padding: Int = 0
)

// Define a data class to store latitude and longitude coordinates
@Serializable
data class Location(val latitude: Double, val longitude: Double)

fun Location.isLocationValid(): Boolean {
    return latitude != 0.0 && longitude != 0.0
}

fun LatLong.toLocation(): Location {
    return Location(latitude, longitude)
}

val defaultMapPos = LatLong(37.7749, 58.8788)

@Composable
expect fun GoogleMaps(
    modifier: Modifier,
    cameraPosition: CameraPosition = CameraPosition(
        target = LatLong(37.7749, 58.8788),
        zoom = 5f
    ),
    units: List<UnitModel> = emptyList(),
    geofences: Map<String, List<P>> = emptyMap(),
    onUnitClick: (UnitModel) -> Unit = {},
    mapType: MapType = MapType.MAP,
    polyline: List<String?>? = emptyList(),
    singleMarker: LatLong? = null
)