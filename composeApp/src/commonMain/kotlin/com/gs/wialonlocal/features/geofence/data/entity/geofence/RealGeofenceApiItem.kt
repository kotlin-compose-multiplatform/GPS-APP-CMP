package com.gs.wialonlocal.features.geofence.data.entity.geofence

import com.gs.wialonlocal.features.geofence.data.entity.B
import kotlinx.serialization.Serializable

@Serializable
data class RealGeofenceApiItem(
    val ar: Double,
    val c: Long,
    val ct: Int,
    val d: String,
    val f: Int,
    val i: Long,
    val icon: String,
    val id: Int,
    val libId: Int,
    val max: Int,
    val min: Int,
    val mt: Int,
    val n: String,
    val p: List<P>,
    val path: String,
    val rid: Int,
    val t: Int,
    val tc: Int,
    val ts: Int,
    val w: Int,
    val b: B? = null
)