package com.gs.wialonlocal.features.report.di

import com.gs.wialonlocal.features.report.data.repository.ReportRepositoryImpl
import com.gs.wialonlocal.features.report.domain.repository.ReportRepository
import com.gs.wialonlocal.features.report.domain.usecase.ReportUseCase
import com.gs.wialonlocal.features.report.presentation.viewmodel.ReportViewModel
import org.koin.dsl.module

val reportModule = module {
    single<ReportRepository> { ReportRepositoryImpl(get(), get()) }
    single { ReportUseCase(get()) }
    factory { ReportViewModel(get()) }
}