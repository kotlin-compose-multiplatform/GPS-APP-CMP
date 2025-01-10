package com.gs.wialonlocal.features.auth.presentation.state

import com.gs.wialonlocal.features.auth.domain.model.LoginModel

data class LoginState(
    val loading: Boolean = false,
    val error: String? = null,
    val data: LoginModel? = null
)
