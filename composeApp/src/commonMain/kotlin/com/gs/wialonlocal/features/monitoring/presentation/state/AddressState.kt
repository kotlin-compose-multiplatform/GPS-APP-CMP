package com.gs.wialonlocal.features.monitoring.presentation.state

data class AddressState(
    val loading: Boolean = false,
    val error: String? = null,
    val data: String? = null
)
