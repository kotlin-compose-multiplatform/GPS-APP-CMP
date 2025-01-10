package com.gs.wialonlocal.features.monitoring.data.entity

import kotlinx.serialization.Serializable

@Serializable
data class TripDetector(
    val gpsCorrection: Int,
    val maxMessagesDistance: Double,
    val minMovingSpeed: Double,
    val minSat: Double,
    val minStayTime: Double,
    val minTripDistance: Double,
    val minTripTime: Double,
    val type: Int
)