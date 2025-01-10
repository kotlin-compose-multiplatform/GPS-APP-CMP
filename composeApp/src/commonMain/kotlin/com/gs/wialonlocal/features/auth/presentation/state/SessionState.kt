package com.gs.wialonlocal.features.auth.presentation.state

import com.gs.wialonlocal.features.global.domain.model.CheckSessionModel

data class SessionState(
    val loading: Boolean = true,
    val error: String? = null,
    val session: CheckSessionModel? = null,
    val checkIndex: Long = 0L
)
