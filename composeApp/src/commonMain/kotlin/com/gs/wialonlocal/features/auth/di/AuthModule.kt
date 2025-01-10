package com.gs.wialonlocal.features.auth.di

import com.gs.wialonlocal.features.auth.data.AuthSettings
import com.gs.wialonlocal.features.auth.data.repository.AuthRepositoryImpl
import com.gs.wialonlocal.features.auth.domain.repository.AuthRepository
import com.gs.wialonlocal.features.auth.domain.usecase.AuthUseCase
import com.gs.wialonlocal.features.auth.presentation.viewmodel.AuthViewModel
import com.russhwolf.settings.Settings
import org.koin.dsl.module

val authModule = module {
    single { Settings() }
    single { AuthSettings(get()) }
    single<AuthRepository> { AuthRepositoryImpl(get(), get()) }
    single { AuthUseCase(get()) }
    factory { AuthViewModel(get(), get(), get()) }
}