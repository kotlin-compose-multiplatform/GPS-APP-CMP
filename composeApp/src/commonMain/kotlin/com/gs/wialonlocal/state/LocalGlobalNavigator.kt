package com.gs.wialonlocal.state

import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.mutableStateOf
import cafe.adriel.voyager.navigator.Navigator

val LocalGlobalNavigator = compositionLocalOf {
    mutableStateOf<Navigator?>(null)
}