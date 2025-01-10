package com.gs.wialonlocal.features.global.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.gs.wialonlocal.core.network.Resource
import com.gs.wialonlocal.features.global.domain.usecase.GlobalUseCase
import com.gs.wialonlocal.features.global.presentation.state.CheckVersionState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class GlobalViewModel(
    private val useCase: GlobalUseCase
) : ViewModel() {
    private val _versionState = MutableStateFlow(CheckVersionState())
    val versionState = _versionState.asStateFlow()

    fun initVersion() {
        if(_versionState.value.version.isNullOrEmpty()) {
            getVersions()
        }
    }

    fun getVersions() {
        viewModelScope.launch {
            useCase.getVersions().onEach {
                when(it) {
                    is Resource.Error -> {
                        _versionState.value = _versionState.value.copy(
                            loading = false,
                            error = it.message,
                            version = it.data
                        )
                    }
                    is Resource.Loading -> {
                        _versionState.value = _versionState.value.copy(
                            loading = true,
                            error = it.message,
                            version = it.data
                        )
                    }
                    is Resource.Success -> {
                        _versionState.value = _versionState.value.copy(
                            loading = false,
                            error = it.message,
                            version = it.data
                        )
                    }
                }
            }.launchIn(this)
        }
    }
}