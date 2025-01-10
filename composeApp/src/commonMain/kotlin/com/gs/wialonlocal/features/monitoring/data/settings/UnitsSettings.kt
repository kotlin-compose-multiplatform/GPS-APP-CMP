package com.gs.wialonlocal.features.monitoring.data.settings

import com.russhwolf.settings.Settings

class UnitsSettings(
    private val settings: Settings
) {
    companion object {
        const val LIST_KEY = "list_key"
    }

    fun saveUnits(ids: List<String>) {
        settings.putString(LIST_KEY, ids
            .joinToString("|")
            .replace("[", "")
            .replace("]", ""))
    }

    fun getUnits(): List<String> {
        return settings.getString(LIST_KEY, "").split("|").filter { it.isNotEmpty() && it!="[" && it!="]" }
    }
}