package com.gs.wialonlocal.features.auth.data.entity

import kotlinx.serialization.Serializable

@Serializable
data class Features(
    val svcs: Svcs,
    val unlim: Int
)