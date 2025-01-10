package com.gs.wialonlocal.common

import android.content.Intent
import android.net.Uri
import android.content.Context
import android.os.Parcelable
import android.util.Log
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.header
import io.ktor.serialization.kotlinx.json.json
import kotlinx.android.parcel.Parcelize
import kotlinx.serialization.json.Json
import okhttp3.OkHttpClient
import java.security.SecureRandom
import java.security.cert.CertificateException
import java.security.cert.X509Certificate
import java.util.concurrent.TimeUnit
import javax.net.ssl.SSLContext
import javax.net.ssl.SSLSocketFactory
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager

actual fun openNavigationApp(endLatitude: Double, endLongitude: Double, context: Any?) {
    val uri = Uri.parse("google.navigation:q=$endLatitude,$endLongitude")
    val intent = Intent(Intent.ACTION_VIEW, uri).apply {
        setPackage("com.google.android.apps.maps")
    }
    intent.resolveActivity((context as Context).packageManager)?.let {
        context.startActivity(intent)
    }
}

class AndroidUrlSharer : UrlSharer {
    override fun shareUrl(url: String, context: Any?) {
        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, url)
        }
        (context as Context).startActivity(Intent.createChooser(intent, "Share URL"))
    }
}

actual fun getUrlSharer(): UrlSharer = AndroidUrlSharer()
actual fun getDevice(): String = "android"

actual fun getVersion(): String = "1.0.2"

actual fun getStoreUrl(): String = "https://play.google.com/store/apps/details?id=com.gs.wialonlocal.android"

actual typealias CommonParcelize = Parcelize

actual typealias CommonParcelable = Parcelable

fun getUnsafeClient(): OkHttpClient {
    val trustAllCerts: Array<TrustManager> = arrayOf(
        object : X509TrustManager {
            override fun checkClientTrusted(chain: Array<X509Certificate>, authType: String) { }
            override fun checkServerTrusted(chain: Array<X509Certificate>, authType: String) { }
            override fun getAcceptedIssuers(): Array<X509Certificate> = arrayOf()
        }
    )

    val sslContext = SSLContext.getInstance("SSL").apply {
        init(null, trustAllCerts, SecureRandom())
    }
    val sslSocketFactory: SSLSocketFactory = sslContext.socketFactory

    return OkHttpClient.Builder()
        .sslSocketFactory(sslSocketFactory, trustAllCerts[0] as X509TrustManager)
        .hostnameVerifier { _, _ -> true }
        .connectTimeout(60, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        .writeTimeout(60, TimeUnit.SECONDS)
        .retryOnConnectionFailure(true) // Enable retries on connection failure
        .build()
}


actual val httpClient: HttpClient = HttpClient(OkHttp) {
    engine {
        preconfigured = getUnsafeClient()
    }
    install(HttpTimeout) {
        requestTimeoutMillis = 100000L
        socketTimeoutMillis = 100000L
        connectTimeoutMillis = 100000L
    }
    defaultRequest {
        header("Content-Type", "application/json")
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
                Log.d("HTTP Client", message)
            }
        }
        level = LogLevel.HEADERS
    }
}