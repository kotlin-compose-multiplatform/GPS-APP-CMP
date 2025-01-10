package com.gs.wialonlocal.features.monitoring.presentation.state

import com.gs.wialonlocal.features.monitoring.domain.model.UnitModel

data class UnitState(
    val loading: Boolean = true,
    val error: String? = null,
    val code: Int? = 500,
    val data: List<UnitModel>? = null
)
