package com.gs.wialonlocal.state

import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.mutableStateOf

val LocalTheme = compositionLocalOf {
    mutableStateOf(false)
}