package com.gs.wialonlocal.core.network

import TestViewModel
import com.gs.wialonlocal.common.httpClient
import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import com.gs.wialonlocal.logger.AppLogger
import io.ktor.client.plugins.HttpTimeout
import org.koin.dsl.module

val AppHttpClient = HttpClient {
    install(HttpTimeout) {
        requestTimeoutMillis = 100000L
        socketTimeoutMillis = 100000L
        connectTimeoutMillis = 100000L
    }

    expectSuccess = true
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
                AppLogger.log(message)
            }
        }
        level = LogLevel.HEADERS
    }
}



val provideHttpClient = module {
    single {
        httpClient
    }
}

val provideViewModel = module {
    factory {
        TestViewModel(get())
    }
}