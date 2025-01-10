package com.gs.wialonlocal.features.global.presentation.state

import com.gs.wialonlocal.features.global.data.entity.GetVersionsItem

data class CheckVersionState(
    val loading: Boolean = true,
    val error: String? = null,
    val version: List<GetVersionsItem>? = null
)
