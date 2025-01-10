package com.gs.wialonlocal.features.report.presentation.viewmodel

import androidx.compose.runtime.mutableStateOf
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.gs.wialonlocal.core.network.Resource
import com.gs.wialonlocal.features.monitoring.domain.model.UnitModel
import com.gs.wialonlocal.features.report.domain.model.TemplateModel
import com.gs.wialonlocal.features.report.domain.usecase.ReportUseCase
import com.gs.wialonlocal.features.report.presentation.state.ExecuteReportState
import com.gs.wialonlocal.features.report.presentation.state.TemplateState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class ReportViewModel(
    private val reportUseCase: ReportUseCase
) : ScreenModel {
    private val _templateState = MutableStateFlow(TemplateState())
    val templateState = _templateState.asStateFlow()

    val selectedTemplate = mutableStateOf<TemplateModel?>(null)
    val selectedUnit = mutableStateOf<UnitModel?>(null)

    val selectedStartDate = mutableStateOf<Long?>(null)
    val selectedEndDate = mutableStateOf<Long?>(null)

    val selectedOrientation = mutableStateOf("landscape") // portrait

    private val _reportState = MutableStateFlow(ExecuteReportState())
    val reportState = _reportState.asStateFlow()

    fun setOrientation(v: String) {
        selectedOrientation.value = v
    }

    fun setStartDate(date: Long?) {
        selectedStartDate.value = date
    }

    fun setEndDate(date: Long?) {
        selectedEndDate.value = date
    }


    fun setSelectedUnit(unitModel: UnitModel?) {
        selectedUnit.value = unitModel
    }

    fun setSelectedTemplate(templateModel: TemplateModel?) {
        selectedTemplate.value = templateModel
    }

    fun initTemplates() {
        if(_templateState.value.templates.isNullOrEmpty()) {
            getTemplates()
        }
    }

    fun execute(onSuccess: ()-> Unit) {
        screenModelScope.launch {
            selectedTemplate.value?.let {
                selectedUnit.value?.let { it1 ->
                    selectedStartDate.value?.let { it2 ->
                        selectedEndDate.value?.let { it3 ->
                            reportUseCase.executeReport(
                                templateModel = it,
                                unitModel = it1,
                                startDate = it2,
                                endDate = it3,
                                orientation = selectedOrientation.value
                            ).onEach { result->
                                when(result) {
                                    is Resource.Error -> {
                                        _reportState.value = _reportState.value.copy(
                                            loading = false,
                                            error = result.message,
                                            pdf = result.data
                                        )
                                    }
                                    is Resource.Loading -> {
                                        _reportState.value = _reportState.value.copy(
                                            loading = true,
                                            error = result.message,
                                            pdf = result.data
                                        )
                                    }
                                    is Resource.Success -> {
                                        println("PDFF SUCCESS")
                                        _reportState.value = _reportState.value.copy(
                                            loading = false,
                                            error = result.message,
                                            pdf = result.data
                                        )
                                        onSuccess()
                                    }
                                }
                            }.launchIn(this)
                        }
                    }
                }
            }
        }
    }

    private fun getTemplates() {
        screenModelScope.launch {
            reportUseCase.getTemplates().onEach {
                when(it) {
                    is Resource.Error -> {
                        _templateState.value = _templateState.value.copy(
                            loading = false,
                            error = it.message,
                            templates = it.data
                        )
                    }
                    is Resource.Loading -> {
                        _templateState.value = _templateState.value.copy(
                            loading = true,
                            error = it.message,
                            templates = it.data
                        )
                    }
                    is Resource.Success -> {
                        _templateState.value = _templateState.value.copy(
                            loading = false,
                            error = it.message,
                            templates = it.data
                        )
                    }
                }
            }.launchIn(this)
        }
    }
}