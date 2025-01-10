package com.gs.wialonlocal.features.auth.presentation.viewmodel

import androidx.compose.runtime.mutableStateOf
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.gs.wialonlocal.core.network.Resource
import com.gs.wialonlocal.features.auth.data.AuthSettings
import com.gs.wialonlocal.features.auth.domain.usecase.AuthUseCase
import com.gs.wialonlocal.features.auth.presentation.state.LoginState
import com.gs.wialonlocal.features.auth.presentation.state.SessionState
import com.gs.wialonlocal.features.global.domain.usecase.GlobalUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class AuthViewModel(
    private val authSettings: AuthSettings,
    private val authUseCase: AuthUseCase,
    private val globalUseCase: GlobalUseCase
): ScreenModel {
    private val _loginState = MutableStateFlow(LoginState())
    val loginState = _loginState.asStateFlow()

    private val _sessionState = MutableStateFlow(SessionState())
    val sessionState = _sessionState.asStateFlow()

    val logoutLoading = mutableStateOf(false)

    fun login(
        onRedirectLogin: () -> Unit = {},
        onSuccess: () -> Unit = {},
        onError: () -> Unit = {}
    ) {
        println("Login checking...")
        screenModelScope.launch {
            authUseCase.login().onEach {
                when(it) {
                    is Resource.Error -> {
                        println("Login error ${it.message}")
                        if(it.message == AuthSettings.NO_TOKEN_MESSAGE) {
                            onRedirectLogin()
                        }
                        onError()
                        _loginState.value = _loginState.value.copy(
                            loading = false,
                            error = it.message,
                            data = it.data
                        )
                    }
                    is Resource.Loading -> {
                        println("Login loading...")
                        _loginState.value = _loginState.value.copy(
                            loading = true,
                            error = it.message,
                            data = it.data
                        )
                    }
                    is Resource.Success -> {
                        println("Login success")
                        onSuccess()
                        _loginState.value = _loginState.value.copy(
                            loading = false,
                            error = it.message,
                            data = it.data
                        )
                    }
                }
            }.launchIn(this)
        }
    }

    fun checkSession(
        onError: (String?) -> Unit = {},
        onSuccess: () -> Unit = {}
    ) {
        println("Session checking...")
        screenModelScope.launch {
            globalUseCase.checkSession().onEach {
                when(it) {
                    is Resource.Error -> {
                        println("Session error ${it.message}")
                        onError(it.message)
                        _sessionState.value = _sessionState.value.copy(
                            loading = false,
                            error = it.message
                        )
                    }
                    is Resource.Loading -> {
                        println("Session loading...")
                        _sessionState.value = _sessionState.value.copy(
                            loading = false,
                            error = it.message
                        )
                    }
                    is Resource.Success -> {
                        println("Session success ${_sessionState.value.checkIndex}")
                        onSuccess()
                        _sessionState.value = _sessionState.value.copy(
                            loading = false,
                            error = it.message,
                            session = it.data,
                            checkIndex = _sessionState.value.checkIndex.plus(1)
                        )
                    }
                }
            }.launchIn(this)
        }
    }

    fun logout(onSuccess: () -> Unit) {
        screenModelScope.launch {
            authUseCase.logout().onEach {
                when(it) {
                    is Resource.Error -> {
                        logoutLoading.value = false
                    }
                    is Resource.Loading -> {
                        logoutLoading.value = true
                    }
                    is Resource.Success -> {
                        onSuccess()
                        logoutLoading.value = false
                    }
                }
            }.launchIn(this)
        }
    }

    fun auth(
        onRedirectLogin: () -> Unit,
        onSuccess: () -> Unit = {}
    ) {
        println("Auth checking...")
        if(authSettings.isTokenExist().not()) {
            println("Login Redirecting...")
            onRedirectLogin()
        } else {
            checkSession(
                onError = {
                    println("Session error")
                    login(
                        onRedirectLogin = onRedirectLogin,
                        onSuccess = {
                            checkSession()
                        },
                        onError = {
                            onRedirectLogin()
                        }
                    )
                },
                onSuccess = {
                    onSuccess()
                }
            )
        }
    }
}