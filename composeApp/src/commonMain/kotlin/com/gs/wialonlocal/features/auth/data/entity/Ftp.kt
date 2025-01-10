package com.gs.wialonlocal.features.auth.data.entity

import kotlinx.serialization.Serializable

@Serializable
data class Ftp(
    val ch: Int,
    val fl: Int,
    val tp: Int
)