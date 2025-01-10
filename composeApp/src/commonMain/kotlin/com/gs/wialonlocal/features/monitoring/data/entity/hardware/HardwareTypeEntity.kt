package com.gs.wialonlocal.features.monitoring.data.entity.hardware

import kotlinx.serialization.Serializable

@Serializable
data class HardwareTypeEntity(
    val flespi_id: Int? = null,
    val flespi_protocol_id: Int? = null,
    val id: Int? = null,
    val name: String? = null,
    val tp: String? = null,
    val uid2: Int? = null,
    val up: String? = null
)