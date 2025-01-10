package com.gs.wialonlocal.features.global.di

import androidx.lifecycle.viewmodel.compose.viewModel
import com.gs.wialonlocal.features.global.data.repository.GlobalRepositoryImpl
import com.gs.wialonlocal.features.global.domain.repository.GlobalRepository
import com.gs.wialonlocal.features.global.domain.usecase.GlobalUseCase
import com.gs.wialonlocal.features.global.presentation.viewmodel.GlobalViewModel
import org.koin.dsl.module

val globalModule = module {
    single<GlobalRepository> { GlobalRepositoryImpl(get(), get()) }
    single { GlobalUseCase(get()) }
    factory { GlobalViewModel(get()) }
}