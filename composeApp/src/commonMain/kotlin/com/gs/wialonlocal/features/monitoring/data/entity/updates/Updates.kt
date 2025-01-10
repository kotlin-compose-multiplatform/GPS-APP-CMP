package com.gs.wialonlocal.features.monitoring.data.entity.updates

import kotlinx.serialization.Serializable

@Serializable
data class Position(
    val t: Long,
    val y: Double,
    val x: Double
)

@Serializable
data class Ignition(
    val from: Position? = null,
    val to: Position? = null,
    val m: Long? = null,
    val f: Int? = null,
    val state: Int? = null,
    val type: Int? = null,
    val hours: Int? = null,
    val switches: Int? = null,
    val value: Int? = null
)

@Serializable
data class Sensor(
    val from: Position? = null,
    val to: Position? = null,
    val m: Long? = null,
    val f: Int? = null,
    val type: Int? = null,
    val value: Double? = null
)

@Serializable
data class Trip(
    val from: Position? = null,
    val to: Position? = null,
    val m: Long? = null,
    val f: Int? = null,
    val state: Int? = null,
    val max_speed: Int? = null,
    val curr_speed: Int? = null,
    val avg_speed: Int? = null,
    val distance: Int? = null,
    val odometer: Int? = null,
    val course: Int? = null,
    val altitude: Int? = null,
    val pos_flags: Int? = null
)

@Serializable
data class Counter(
    val from: Position? = null,
    val to: Position? = null,
    val m: Long? = null,
    val f: Int? = null,
    val engine_hours: Int? = null,
    val mileage: Int? = null,
    val bytes: Int? = null
)

//@Serializable
//data class Lls(
//    // Assuming this is an empty object; adjust if necessary
//)

@Serializable
data class Data(
    val ignition: Map<String, Ignition?>? = null,
    val sensors: Map<String, Sensor>? = null,
    val trips: Trip? = null,
    val counters: Counter? = null,
//    val lls: Lls? = null
)
