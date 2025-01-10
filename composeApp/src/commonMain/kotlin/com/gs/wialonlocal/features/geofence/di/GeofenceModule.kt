package com.gs.wialonlocal.features.geofence.di

import com.gs.wialonlocal.features.geofence.data.repository.GeofenceRepositoryImpl
import com.gs.wialonlocal.features.geofence.domain.repository.GeofenceRepository
import com.gs.wialonlocal.features.geofence.domain.usecase.GeofenceUseCase
import com.gs.wialonlocal.features.geofence.presentation.viewmodel.GeofenceViewModel
import org.koin.dsl.module

val geoFenceModule = module {
    single<GeofenceRepository> { GeofenceRepositoryImpl(get(), get()) }
    single { GeofenceUseCase(get()) }
    factory { GeofenceViewModel(get()) }
}