package com.gs.wialonlocal.features.monitoring.presentation.state

import com.gs.wialonlocal.features.monitoring.data.entity.hardware.HardwareTypeEntity

data class HardwareTypeState(
    val loading: Boolean = true,
    val error: String? = null,
    val types: List<HardwareTypeEntity>? = null
)
