package com.gs.wialonlocal.features.monitoring.presentation.state

import com.gs.wialonlocal.features.monitoring.data.entity.history.Trip
import com.gs.wialonlocal.features.monitoring.data.entity.history.TripsResponse

data class LoadEventState(
    val loading: Boolean = true,
    val error: String? = null,
    val data: Pair<List<Trip>, List<Trip>>? = null
)
