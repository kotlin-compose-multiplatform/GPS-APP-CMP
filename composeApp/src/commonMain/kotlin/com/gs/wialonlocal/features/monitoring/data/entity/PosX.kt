package com.gs.wialonlocal.features.monitoring.data.entity

import kotlinx.serialization.Serializable

@Serializable
data class PosX(
    val c: Int,
    val f: Int,
    val lc: Long,
    val s: Int,
    val sc: Int,
    val t: Int? = null,
    val x: Double? = null,
    val y: Double? = null,
    val z: String? = null
)