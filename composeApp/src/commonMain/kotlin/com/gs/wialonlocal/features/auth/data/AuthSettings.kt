package com.gs.wialonlocal.features.auth.data

import com.gs.wialonlocal.features.auth.domain.model.AuthModel
import com.russhwolf.settings.Settings

class AuthSettings(
    private val settings: Settings
) {
    companion object {
        const val TOKEN_KEY = "token"
        const val NAME_KEY = "name"
        const val REFRESH_TOKEN_KEY = "refresh_token"
        const val PHONE_KEY = "phone"
        const val ID_KEY = "id"
        const val USERNAME_KEY = "username"
        const val CREATED_AT_KEY = "created_at"
        const val ACCESS_TYPE_KEY = "access_type"
        const val SESSION_ID_KEY = "session_id"
        const val NO_TOKEN_MESSAGE = "no_token_message"
        const val NO_SESSION_MESSAGE = "no_session_id"
        const val GIS_SID = "gis_sid"
    }

    fun saveSession(id: String) {
        settings.putString(SESSION_ID_KEY, id)
    }

    fun getSessionId(): String = settings.getString(SESSION_ID_KEY, "")

    fun saveGisId(gisId: String) {
        settings.putString(GIS_SID, gisId)
    }

    fun getGisSid(): String = settings.getString(GIS_SID, "")

    fun isTokenExist(): Boolean {
        return settings.getString(TOKEN_KEY, "").trim().isNotEmpty()
    }

    fun isSessionIdExist(): Boolean {
        return settings.getString(SESSION_ID_KEY, "").trim().isNotEmpty()
    }

    fun saveToken(token: String) {
        settings.putString(TOKEN_KEY, token)
    }

    fun getToken(): String {
        return settings.getString(TOKEN_KEY, "")
    }

    fun getName(): String {
        return settings.getString(NAME_KEY, "")
    }

    fun saveName(name: String) {
        settings.putString(NAME_KEY, name)
    }

    fun saveRefreshToken(token: String) {
        settings.putString(REFRESH_TOKEN_KEY, token)
    }

    fun getRefreshToken(): String {
        return settings.getString(REFRESH_TOKEN_KEY, "")
    }

    fun savePhone(phone: String) {
        settings.putString(PHONE_KEY, phone)
    }

    fun getPhone(): String {
        return settings.getString(PHONE_KEY, "")
    }

    fun saveId(id: String) {
        settings.putString(ID_KEY, id)
    }

    fun getId(): String {
        return settings.getString(ID_KEY, "")
    }

    fun getUserName(): String {
        return settings.getString(USERNAME_KEY, "")
    }

    fun saveUserName(username: String) {
        settings.putString(USERNAME_KEY, username)
    }

    fun getCreatedAt(): String {
        return settings.getString(CREATED_AT_KEY, "")
    }

    fun saveCreatedAt(createdAt: String) {
        settings.putString(CREATED_AT_KEY, createdAt)
    }

    fun saveAccessType(type: Int) {
        settings.putInt(ACCESS_TYPE_KEY, type)
    }

    fun getAccessType(): Int {
        return settings.getInt(ACCESS_TYPE_KEY, -1)
    }

    fun getUser(): AuthModel {
        return AuthModel(
            id = getId(),
            name = getName(),
            token = getToken(),
            phone = getPhone(),
            username = getUserName(),
            createdAt = getCreatedAt(),
            refreshToken = getRefreshToken(),
            accessType = getAccessType()
        )
    }

    fun saveUser(user: AuthModel) {
        saveUserName(user.username)
        saveId(user.id)
        savePhone(user.phone)
        saveName(user.name)
        saveToken(user.token)
        saveAccessType(user.accessType)
        saveCreatedAt(user.createdAt)
        saveRefreshToken(user.refreshToken)
    }

    fun logout() {
        saveUserName("")
        saveId("")
        savePhone("")
        saveName("")
        saveToken("")
        saveAccessType(-1)
        saveCreatedAt("")
        saveRefreshToken("")
        saveSession("")
        saveGisId("")
    }
}