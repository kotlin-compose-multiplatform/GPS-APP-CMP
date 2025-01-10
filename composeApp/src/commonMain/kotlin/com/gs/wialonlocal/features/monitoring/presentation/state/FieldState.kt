package com.gs.wialonlocal.features.monitoring.presentation.state

import com.gs.wialonlocal.features.monitoring.data.entity.history.CustomFields

data class FieldState(
    val loading: Boolean = true,
    val error: String? = null,
    val data: CustomFields? = null
)
