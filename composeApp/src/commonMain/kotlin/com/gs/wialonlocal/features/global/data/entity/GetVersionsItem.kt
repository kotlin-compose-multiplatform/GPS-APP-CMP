package com.gs.wialonlocal.features.global.data.entity

import kotlinx.serialization.Serializable

@Serializable
data class GetVersionsItem(
    val device: String,
    val required: Boolean,
    val version: String
)