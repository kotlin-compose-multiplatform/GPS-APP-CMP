package com.gs.wialonlocal.features.monitoring.data.entity

import kotlinx.serialization.Serializable

@Serializable
data class P(
    val event_io_id: Int? = null,
    val io_1: Int? = null,
    val io_16: Int? = null,
    val io_239: Int? = null,
    val io_24: Int? = null,
    val io_240: Int? = null,
    val io_66: Int? = null,
    val io_67: Int? = null,
    val io_68: Int? = null,
    val prior: Int? = null,
    val pwr_ext: String? = null,
    val pwr_int: Double? = null,
    val total_io: Int? = null
)