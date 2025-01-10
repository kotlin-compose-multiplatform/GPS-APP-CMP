package com.gs.wialonlocal.features.settings.di

import com.gs.wialonlocal.features.settings.data.settings.AppSettings
import org.koin.dsl.module

val settingsModule = module {
    single { AppSettings(get()) }
}