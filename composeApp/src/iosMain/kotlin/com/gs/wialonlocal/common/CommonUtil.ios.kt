package com.gs.wialonlocal.common

import io.ktor.client.HttpClient
import io.ktor.client.engine.darwin.Darwin
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.serialization.kotlinx.json.json
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.serialization.json.Json
import platform.MapKit.*
import platform.CoreLocation.*
import platform.Foundation.NSURL
import platform.Foundation.NSValue
import platform.UIKit.UIApplication
// iOS module: UrlSharerIOS.kt
import platform.UIKit.UIActivityViewController
import platform.UIKit.UIViewController
import platform.UIKit.UIScreen
import platform.UIKit.UIWindow
import platform.darwin.NSObject

fun shareUrlWithoutUIViewController(urlString: String) {
    // Get the root UIViewController to present the share dialog
    val currentViewController = getCurrentRootViewController()

    // Create a NSURL from the string
    val url = NSURL.URLWithString(urlString)

    // Create a UIActivityViewController for sharing
    val activityViewController = UIActivityViewController(activityItems = listOf(url), applicationActivities = null)

    // Present the share sheet
    currentViewController?.presentViewController(activityViewController, animated = true, completion = null)
}

// Helper function to retrieve the root view controller
fun getCurrentRootViewController(): UIViewController? {
    val keyWindow = UIApplication.sharedApplication.keyWindow
    var rootViewController = keyWindow?.rootViewController

    // Traverse the presented view controllers to get the top one
    while (rootViewController?.presentedViewController != null) {
        rootViewController = rootViewController.presentedViewController
    }

    return rootViewController
}


class IOSUrlSharer : UrlSharer {
    override fun shareUrl(url: String, context: Any?) {
        shareUrlWithoutUIViewController(url)
    }
}

actual fun getUrlSharer(): UrlSharer = IOSUrlSharer()


@OptIn(ExperimentalForeignApi::class)
actual fun openNavigationApp(endLatitude: Double, endLongitude: Double, context: Any?) {
//    val latitude: CLLocationDegrees = endLatitude
//    val longitude: CLLocationDegrees = endLongitude
//    val regionDistance: CLLocationDistance = 10000.0
//
//    val coordinates = CLLocationCoordinate2DMake(latitude, longitude)
//    val regionSpan = MKCoordinateRegionMakeWithDistance(coordinates, regionDistance, regionDistance)
//
//    val options = mapOf(
//        MKLaunchOptionsMapCenterKey to NSValue.valueWithMKCoordinate(regionSpan.center),
//        MKLaunchOptionsMapSpanKey to NSValue.valueWithMKCoordinateSpan(regionSpan.span)
//    )

    UIApplication.sharedApplication.openURL(NSURL(
        string = "http://maps.apple.com/maps?daddr=${endLatitude},${endLongitude}"
    ))
//
//    val placemark = MKPlacemark(coordinate = coordinates, addressDictionary = null)
//    val mapItem = MKMapItem(placemark = placemark)
//    mapItem.name = "Destination"
//    mapItem.openInMapsWithLaunchOptions(launchOptions = options)
}

actual fun getDevice(): String = "ios"

actual fun getVersion(): String = "1.0.0"

actual fun getStoreUrl(): String = "https://apps.apple.com/me/app/wialon-local/id1011136393"

actual interface CommonParcelable

actual val httpClient: HttpClient = HttpClient(Darwin) {

    install(HttpTimeout) {
        requestTimeoutMillis = 100000L
        socketTimeoutMillis = 100000L
        connectTimeoutMillis = 100000L
    }

    install(ContentNegotiation) {
        json(Json {
            prettyPrint = true
            isLenient = true
            ignoreUnknownKeys = true
        })
    }
    install(Logging) {
        logger = object: Logger {
            override fun log(message: String) {
                println("API CALL: $message")
            }
        }
        level = LogLevel.BODY
    }
}
