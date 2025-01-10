package com.gs.wialonlocal.common

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.viewinterop.UIKitInteropProperties
import androidx.compose.ui.viewinterop.UIKitView
import cocoapods.GoogleMaps.*
import cocoapods.Google_Maps_iOS_Utils.*
import cocoapods.GoogleMaps.GMSAdvancedMarker.Companion.markerImageWithColor
import cocoapods.GoogleMaps.GMSCameraPosition
import cocoapods.GoogleMaps.GMSCameraUpdate
import cocoapods.GoogleMaps.GMSCircle
import cocoapods.GoogleMaps.GMSCoordinateBounds
import cocoapods.GoogleMaps.GMSFeatureLayer
import cocoapods.GoogleMaps.GMSMapStyle
import cocoapods.GoogleMaps.GMSMapView
import cocoapods.GoogleMaps.GMSMapViewDelegateProtocol
import cocoapods.GoogleMaps.GMSMapViewOptions
import cocoapods.GoogleMaps.GMSMapViewType
import cocoapods.GoogleMaps.GMSMarker
import cocoapods.GoogleMaps.GMSMutablePath
import cocoapods.GoogleMaps.GMSPath
import cocoapods.GoogleMaps.GMSPolyline
import cocoapods.GoogleMaps.animateWithCameraUpdate
import cocoapods.GoogleMaps.kGMSTypeHybrid
import cocoapods.GoogleMaps.kGMSTypeNormal
import cocoapods.GoogleMaps.kGMSTypeSatellite
import com.gs.wialonlocal.core.network.AppHttpClient
import com.gs.wialonlocal.features.geofence.data.entity.geofence.P
import com.gs.wialonlocal.features.monitoring.data.entity.history.Trip
import com.gs.wialonlocal.features.monitoring.domain.model.UnitModel
import com.gs.wialonlocal.features.settings.data.settings.MapType
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import kotlinx.cinterop.CValue
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.addressOf
import kotlinx.cinterop.cValue
import kotlinx.cinterop.usePinned
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import platform.CoreLocation.CLLocationCoordinate2D
import platform.CoreLocation.CLLocationCoordinate2DMake
import platform.CoreLocation.CLLocationDegrees
import platform.Foundation.NSData
import platform.Foundation.NSURL
import platform.Foundation.NSURLRequest
import platform.Foundation.NSURLSession
import platform.Foundation.create
import platform.Foundation.dataWithBytes
import platform.MapKit.MKMapView
import platform.UIKit.UIColor
import platform.UIKit.UIImage
import platform.darwin.NSObject
import kotlin.coroutines.CoroutineContext
import platform.CoreGraphics.*
import platform.MapKit.MKCircle
import platform.UIKit.UIColor.Companion.blueColor
import platform.UIKit.UIColor.Companion.greenColor
import platform.UIKit.UIGraphicsBeginImageContextWithOptions
import platform.UIKit.UIGraphicsEndImageContext
import platform.UIKit.UIGraphicsGetImageFromCurrentImageContext
import platform.darwin.NSUInteger

// Coroutine scope for main thread operations
val uiScope: CoroutineScope = CoroutineScope(Dispatchers.Main)

val client = AppHttpClient

val imageCache = mutableStateMapOf<String, UIImage?>()

@OptIn(ExperimentalForeignApi::class)
val oldPolylines = mutableStateOf<List<GMSPolyline>>(emptyList())


// A list to keep track of markers
@OptIn(ExperimentalForeignApi::class)
val markers = mutableListOf<GMSMarker>()

@OptIn(ExperimentalForeignApi::class)
fun clearMarkers() {
    // Remove all existing markers from the map
    markers.forEach { marker ->
        marker.map = null // This will remove the marker from the map
    }
    markers.clear() // Clear the list after removing the markers
}

@OptIn(ExperimentalForeignApi::class)
fun resizeImage(image: UIImage, targetWidth: Double, targetHeight: Double): UIImage? {
    val newSize = CGSizeMake(targetWidth, targetHeight)
    UIGraphicsBeginImageContextWithOptions(newSize, false, 0.0)

    image.drawInRect(CGRectMake(0.0, 0.0, targetWidth, targetHeight))
    val resizedImage = UIGraphicsGetImageFromCurrentImageContext()
    UIGraphicsEndImageContext()

    return resizedImage
}

@OptIn(ExperimentalForeignApi::class)
suspend fun downloadImage(url: String, targetWidth: Double, targetHeight: Double): UIImage? {
    // Check cache first
    imageCache[url]?.let { cachedImage ->
        return cachedImage
    }

    // If not cached, download the image
    return try {
        val response = client.get(url)
        val byteArray = response.body<ByteArray>()
        val nsData = byteArray.usePinned { pinned ->
            NSData.dataWithBytes(pinned.addressOf(0), byteArray.size.toULong())
        }
        val image = UIImage.imageWithData(nsData)?.let { resizeImage(it, targetWidth, targetHeight) }

        // Store the image in cache
        imageCache[url] = image
        image
    } catch (e: Exception) {
        println("Failed to download image: ${e.message}")
        null
    }
}

@OptIn(ExperimentalForeignApi::class)
fun calculateBounds(latLngs: List<CValue<CLLocationCoordinate2D>>): GMSCoordinateBounds? {
    if (latLngs.isEmpty()) return null

    var bounds: GMSCoordinateBounds? = null
    latLngs.forEach { coordinate ->
        bounds = if (bounds == null) {
            GMSCoordinateBounds(coordinate, coordinate)
        } else {
            bounds?.includingCoordinate(coordinate)
        }
    }
    return bounds
}

@OptIn(ExperimentalForeignApi::class)
fun moveToBounds(
    mapView: GMSMapView,
    bounds: GMSCoordinateBounds,
    coroutineScope: CoroutineScope,
    padding: Int
) {
    val cameraUpdate = GMSCameraUpdate.fitBounds(bounds, padding.toDouble())

    coroutineScope.launch(Dispatchers.Main) {
        mapView.animateWithCameraUpdate(cameraUpdate)
    }
}

@OptIn(ExperimentalForeignApi::class)
fun setMarkerIconFromUrlKtor(
    id: String,
    urlString: String,
    latitude: Double,
    longitude: Double,
    mapView: GMSMapView,
    title: String
) {
    uiScope.launch {

        val image = downloadImage(urlString, 30.0,30.0)  // Download image using Ktor
        if (image != null) {
            // Set up the marker with the downloaded image
            val position = CLLocationCoordinate2DMake(latitude = latitude, longitude = longitude)
            val marker = GMSMarker()

            marker.position = position
            marker.title = title
            marker.icon = image
            marker.map = mapView
            marker.userData = id


            // Add the new marker to the list
            markers.add(marker)
        }
    }
}

@OptIn(ExperimentalForeignApi::class)
var startMarker: GMSMarker? = null

@OptIn(ExperimentalForeignApi::class)
var endMarker: GMSMarker? = null

@OptIn(ExperimentalForeignApi::class)
var parkingMarker: GMSMarker? = null

@OptIn(ExperimentalForeignApi::class)
val geofenceCircles = mutableListOf<GMSCircle>()


@OptIn(ExperimentalForeignApi::class)
val geofencePolygons =  mutableListOf<GMSPolygon>()

// Delegate to handle marker info window click events
fun UIColorFromHex(hex: Long, alpha: Double = 1.0): UIColor {
    val red = ((hex shr 16) and 0xFF) / 255.0
    val green = ((hex shr 8) and 0xFF) / 255.0
    val blue = (hex and 0xFF) / 255.0

    return UIColor(red = red, green = green, blue = blue, alpha = alpha)
}



@OptIn(ExperimentalForeignApi::class)
@Composable
actual fun GoogleMaps(
    modifier: Modifier,
    cameraPosition: CameraPosition,
    units: List<UnitModel>,
    geofences: Map<String, List<P>>,
    onUnitClick: (UnitModel) -> Unit,
    mapType: MapType,
    polyline: List<String?>?,
    singleMarker: LatLong?
) {
    val googleMapView = rememberSaveable {
        GMSMapView().apply {
            setMyLocationEnabled(true)
            settings.myLocationButton = true
            settings.setMyLocationButton(true)
            settings.setScrollGestures(true)
            settings.setZoomGestures(true)
            settings.setCompassButton(true)
//            this.setMapStyle(
//                GMSMapStyle.styleWithJSONString(
//                    mapStyle1(),
//                    error = null
//                )
//            )
        }
    }

    fun moveCamera(latitude: Double, longitude: Double, zoom: Float) {
        googleMapView.camera = GMSCameraPosition.cameraWithTarget(
            CLLocationCoordinate2DMake(latitude = latitude, longitude = longitude),
            zoom
        )
    }

    LaunchedEffect(true) {
        googleMapView.camera = GMSCameraPosition.cameraWithTarget(
            CLLocationCoordinate2DMake(latitude = cameraPosition.target.latitude, longitude = cameraPosition.target.longitude),
            cameraPosition.zoom
        )
    }

    LaunchedEffect(mapType) {
        when(mapType) {
            MapType.SATELLITE -> {
                googleMapView.mapType = kGMSTypeSatellite
            }
            MapType.HYBRID -> {
                googleMapView.mapType = kGMSTypeHybrid
            }
            else -> {
                googleMapView.mapType = kGMSTypeNormal
            }
        }
    }

    LaunchedEffect(singleMarker) {
        singleMarker?.let {
            parkingMarker?.map = null
            val position = CLLocationCoordinate2DMake(latitude = singleMarker.latitude, longitude = singleMarker.longitude)
            parkingMarker = addMarker(
                mapView = googleMapView,
                position = position,
                iconName = "points",  // Ensure this image exists in assets
                description = "Start"
            )

            moveCamera(singleMarker.latitude, singleMarker.longitude, 14f)
        }
    }

    LaunchedEffect(geofences) {

        geofenceCircles.forEach { it.map = null }
        geofencePolygons.forEach { it.map = null }

        geofenceCircles.clear()
        geofencePolygons.clear()


        geofences.forEach { entry ->
            entry.value.forEach { geofence ->
                val circle = GMSCircle()
                circle.position = CLLocationCoordinate2DMake(geofence.y, geofence.x)
                circle.radius = geofence.r.toDouble()
                circle.strokeColor = UIColor.redColor
                circle.fillColor = UIColor.greenColor.colorWithAlphaComponent(0.33)
                circle.map = googleMapView

                geofenceCircles.add(circle)
            }

            // Create polygons if geofence has more than 2 points
            if (entry.value.size > 2) {
                val path = GMSMutablePath()
                entry.value.forEach {
                    path.addLatitude(it.y, longitude = it.x)
                }
                val polygon = GMSPolygon()
                polygon.path = path
                polygon.strokeColor = UIColor.clearColor
                polygon.fillColor = UIColor.blueColor.colorWithAlphaComponent(0.33)
                polygon.map = googleMapView

                geofencePolygons.add(polygon)
            }
        }

    }



    LaunchedEffect(polyline) {
        // Add Polyline if present

        if(polyline.isNullOrEmpty().not()) {
            parkingMarker?.map = null

            polyline?.let { path->
                val coordinates: List<CValue<CLLocationCoordinate2D>> = path.flatMap { encodedPath ->
                    GMSPath.pathFromEncodedPath(encodedPath!!)?.let { gmsPath ->
                        (0 until gmsPath.count().toInt()).map { index -> gmsPath.coordinateAtIndex(
                            index.toULong()
                        ) }
                    } ?: emptyList()
                }

                val bounds = calculateBounds(coordinates)
                bounds?.let {
                    moveToBounds(googleMapView, it, CoroutineScope(Dispatchers.Main), padding = 20)
                }
            }
        }

        startMarker?.map = null
        endMarker?.map = null

        oldPolylines.value.forEach { p->
            p.map = null
        }
        oldPolylines.value = emptyList()
        polyline?.forEach { p ->
            val decodedPath = GMSPath.pathFromEncodedPath(p!!) ?: return@forEach
            val pol = GMSPolyline.polylineWithPath(decodedPath)
            pol.strokeColor = UIColorFromHex(0xFF009AFF)
            pol.strokeWidth = 4.0
            pol.map = googleMapView
            oldPolylines.value = oldPolylines.value.plus(pol)


        }

        if(polyline.isNullOrEmpty().not()) {
            try {
                val decodedPath = GMSPath.pathFromEncodedPath(polyline?.first()!!)
                if (decodedPath!!.count() > 0u) {
                    val startPosition = decodedPath.coordinateAtIndex(0u)
                    startMarker = addMarker(
                        mapView = googleMapView,
                        position = startPosition,
                        iconName = "points",  // Ensure this image exists in assets
                        description = "Start"
                    )
                }
            } catch (ex: Exception) {
                ex.printStackTrace()
            }

           try {
               val decodedPath = GMSPath.pathFromEncodedPath(polyline?.last()!!)
               if (decodedPath!!.count() > 0u) {
                   val endPosition = decodedPath.coordinateAtIndex(decodedPath.count() - 1u)
                   endMarker = addMarker(
                       mapView = googleMapView,
                       position = endPosition,
                       iconName = "finish2",  // Ensure this image exists in assets
                       description = "End"
                   )
               }
           } catch (ex: Exception) {
               ex.printStackTrace()
           }
        }
    }

    LaunchedEffect(units) {
        clearMarkers() // Remove old markers
        units.forEachIndexed { index, unitModel ->
            setMarkerIconFromUrlKtor(
                id = unitModel.id,
                urlString = unitModel.image,
                latitude = unitModel.latitude,
                longitude = unitModel.longitude,
                mapView = googleMapView, // Your GMSMapView instance
                title = unitModel.carNumber
            )

            val path = GMSMutablePath()
            // Gradient colors
            val startColor = Color.Gray
            val endColor = Color.Green

            unitModel.trips.forEach {
                path.addLatitude(it.latitude, it.longitude)
            }

            val poly = GMSPolyline.polylineWithPath(path)
            poly.strokeWidth = 5.0
            poly.geodesic = true
            poly.strokeColor = greenColor
            poly.map = googleMapView


        }
    }

    // Only used to track selected marker
//    val mapMarkers = remember(units) { mutableStateMapOf<String,GMSMarker>() }
//    var selectedMarker by remember(googleMapView.selectedMarker) { mutableStateOf(googleMapView.selectedMarker) }
    val delegate = remember { object : NSObject(), GMSMapViewDelegateProtocol {

        // Note: this shows an error, but it compiles and runs fine(!)
        override fun mapView(
            mapView: GMSMapView,
            @Suppress("PARAMETER_NAME_CHANGED_ON_OVERRIDE")  // found this hacky fix was found on jetbrains site
            didTapInfoWindowOfMarker: GMSMarker
        ) {
            val userData = didTapInfoWindowOfMarker.userData()
            if (userData != null) {
                onUnitClick(units.first { it.id == userData })
            }
        }

    }}

    UIKitView(
        modifier = Modifier.fillMaxSize(),
        factory = {
            googleMapView.apply {

                setDelegate(delegate)
//                this.selectedMarker = selectedMarker
                this.setMyLocationEnabled(true)
            }

            googleMapView
        },
        update = { view->

        },
        properties = UIKitInteropProperties(
            isInteractive = true,
            isNativeAccessibilityEnabled = true
        )
    )
}

@OptIn(ExperimentalForeignApi::class)
private fun addMarker(
    mapView: GMSMapView,
    position: CValue<CLLocationCoordinate2D>,
    iconName: String,
    description: String
): GMSMarker {
    val marker = GMSMarker().apply {
        this.position = position
        this.icon = UIImage.imageNamed(iconName)
        this.title = description
        this.map = mapView
    }
    return marker
}
