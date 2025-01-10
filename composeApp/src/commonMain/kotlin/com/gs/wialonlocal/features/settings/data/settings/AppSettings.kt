package com.gs.wialonlocal.features.settings.data.settings

import com.gs.wialonlocal.core.locale.Locales
import com.russhwolf.settings.Settings

enum class AppTheme {
    DARK,
    LIGHT
}

enum class MapType {
    MAP,
    SATELLITE,
    HYBRID
}

class AppSettings(
    private val settings: Settings
) {
    companion object {
        const val LANGUAGE_KEY = "app_language"
        const val THEME_KEY = "app_theme"
        const val MAP_TYPE_KEY = "app_map_type"
    }

    /**
     * Save Language function can only save [Locales.TM],[Locales.EN],[Locales.RU]
     */
    fun saveLanguage(language: String) {
        settings.putString(LANGUAGE_KEY, language)
    }

    /**
     * This function can return this values: [Locales.TM],[Locales.EN],[Locales.RU]
     */
    fun getLanguage(): String {
        return settings.getString(LANGUAGE_KEY, Locales.RU)
    }


    /**
     * SAVE THEME CAN ONLY SAVE THIS VALUES (system,light,dark)
     */
    fun saveTheme(theme: String) {
        settings.putString(THEME_KEY, theme)
    }


    /**
     * This function return [AppTheme] enum class
     */
    fun getTheme(): AppTheme {
       return when(settings.getString(THEME_KEY, "light")) {
           "dark" -> AppTheme.DARK
           else -> AppTheme.LIGHT
        }
    }


    /**
     * This function can only save this values: map,satellite,hybrid
     */
    fun saveMapType(type: String) {
        settings.putString(MAP_TYPE_KEY, type)
    }

    /**
     * This function this enum class [MapType]
     */
    fun getMapType(): MapType {
        return when(settings.getString(MAP_TYPE_KEY, "map")) {
            "satellite" -> MapType.SATELLITE
            "hybrid" -> MapType.HYBRID
            else -> MapType.MAP
        }
    }
}