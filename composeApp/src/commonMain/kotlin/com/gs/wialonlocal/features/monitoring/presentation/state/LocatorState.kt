package com.gs.wialonlocal.features.monitoring.presentation.state

import com.gs.wialonlocal.features.monitoring.data.entity.locator.LocatorResponse

data class LocatorState(
    val loading: Boolean = false,
    val error: String? = null,
    val result: LocatorResponse? = null
)
