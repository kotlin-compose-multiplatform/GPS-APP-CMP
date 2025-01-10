package com.gs.wialonlocal.features.auth.data.entity

import kotlinx.serialization.Serializable

@Serializable
data class Env(
    val max_http_buff: Int
)