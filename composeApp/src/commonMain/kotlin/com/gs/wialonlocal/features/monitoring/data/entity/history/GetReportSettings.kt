package com.gs.wialonlocal.features.monitoring.data.entity.history

import kotlinx.serialization.Serializable

@Serializable
data class GetReportSettings(
    val dailyEngineHoursRate: Int,
    val fuelRateCoefficient: Int,
    val maxMessagesInterval: Int,
    val mileageCoefficient: Int,
    val speedLimit: Int,
    val speedingMinDuration: Int,
    val speedingMode: Int,
    val speedingTolerance: Int,
    val urbanMaxSpeed: Int
)