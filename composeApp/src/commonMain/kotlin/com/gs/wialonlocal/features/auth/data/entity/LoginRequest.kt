package com.gs.wialonlocal.features.auth.data.entity

import kotlinx.serialization.Serializable

@Serializable
data class LoginRequest(
    val appName: String,
    val checkService: String,
    val token: String
)