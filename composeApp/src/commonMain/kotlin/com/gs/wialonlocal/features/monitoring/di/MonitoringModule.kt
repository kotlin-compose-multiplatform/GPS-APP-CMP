package com.gs.wialonlocal.features.monitoring.di

import com.gs.wialonlocal.features.auth.data.AuthSettings
import com.gs.wialonlocal.features.monitoring.data.repository.MonitoringRepositoryImpl
import com.gs.wialonlocal.features.monitoring.data.settings.UnitsSettings
import com.gs.wialonlocal.features.monitoring.domain.repository.MonitoringRepository
import com.gs.wialonlocal.features.monitoring.domain.usecase.MonitoringUseCase
import com.gs.wialonlocal.features.monitoring.presentation.viewmodel.MonitoringViewModel
import com.russhwolf.settings.Settings
import org.koin.dsl.module

val monitoringModule = module {
    single<MonitoringRepository> {
        MonitoringRepositoryImpl(get(), get(), get())
    }
    single {
        MonitoringUseCase(get())
    }
    factory {
        MonitoringViewModel(get())
    }
    single {
        UnitsSettings(get())
    }
}