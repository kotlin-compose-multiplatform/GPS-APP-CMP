package com.gs.wialonlocal.common

import io.ktor.client.HttpClient

expect fun openNavigationApp(endLatitude: Double, endLongitude: Double, context: Any?)

// Shared module: Common.kt
interface UrlSharer {
    fun shareUrl(url: String, context: Any?)
}

expect fun getUrlSharer(): UrlSharer

expect fun getDevice(): String
expect fun getVersion(): String
expect fun getStoreUrl(): String

@OptIn(ExperimentalMultiplatform::class)
@OptionalExpectation
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.BINARY)
expect annotation class CommonParcelize()

expect interface CommonParcelable

expect val httpClient: HttpClient

