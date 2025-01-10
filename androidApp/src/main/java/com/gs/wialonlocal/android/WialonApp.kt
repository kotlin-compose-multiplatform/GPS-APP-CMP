package com.gs.wialonlocal.android

import android.app.Application
import android.content.Context

class WialonApp: Application() {

    companion object {
        var INSTANCE: Context? = null
    }

    override fun onCreate() {
        super.onCreate()
        WialonApp.INSTANCE = this
    }
}