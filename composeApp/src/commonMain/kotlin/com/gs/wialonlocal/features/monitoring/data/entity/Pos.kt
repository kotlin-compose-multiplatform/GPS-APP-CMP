package com.gs.wialonlocal.features.monitoring.data.entity

import kotlinx.serialization.Serializable

@Serializable
data class Pos(
    val c: Int,
    val s: Double,
    val sc: Int,
    val x: Double? = null,
    val y: Double? = null,
    val z: Double? = null
)