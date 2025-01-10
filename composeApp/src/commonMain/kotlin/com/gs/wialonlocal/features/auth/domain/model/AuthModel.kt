package com.gs.wialonlocal.features.auth.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class AuthModel(
    val id: String,
    val name: String,
    val token: String,
    val phone: String,
    val username: String,
    val createdAt: String,
    val refreshToken: String,
    val accessType: Int
)
