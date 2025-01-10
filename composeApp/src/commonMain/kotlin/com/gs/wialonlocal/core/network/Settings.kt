package com.gs.wialonlocal.core.network

import com.russhwolf.settings.ObservableSettings
import com.russhwolf.settings.Settings
import org.koin.dsl.module

val provideSettings = module {
    single {
        Settings()
    }
}