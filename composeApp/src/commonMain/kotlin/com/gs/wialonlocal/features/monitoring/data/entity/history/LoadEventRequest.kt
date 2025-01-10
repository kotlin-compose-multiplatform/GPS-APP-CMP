package com.gs.wialonlocal.features.monitoring.data.entity.history

data class LoadEventRequest(
    val itemId: String,
    val timeFrom: Long,
    val timeTo: Long
)
