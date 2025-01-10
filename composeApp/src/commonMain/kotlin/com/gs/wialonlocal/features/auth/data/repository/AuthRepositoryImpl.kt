package com.gs.wialonlocal.features.auth.data.repository

import com.gs.wialonlocal.core.constant.Constant
import com.gs.wialonlocal.core.network.Resource
import com.gs.wialonlocal.features.auth.data.AuthSettings
import com.gs.wialonlocal.features.auth.data.entity.LoginResponse
import com.gs.wialonlocal.features.auth.domain.model.LoginModel
import com.gs.wialonlocal.features.auth.domain.repository.AuthRepository
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.forms.FormDataContent
import io.ktor.client.request.post
import io.ktor.http.Parameters
import io.ktor.http.append
import io.ktor.util.InternalAPI
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class AuthRepositoryImpl(
    private val authSettings: AuthSettings,
    private val httpClient: HttpClient
): AuthRepository {
    @OptIn(InternalAPI::class)
    override suspend fun login(): Flow<Resource<LoginModel>> = flow {
        if(authSettings.isTokenExist()) {
            emit(Resource.Loading())
            try {
                val result = httpClient.post("${Constant.BASE_URL}/wialon/ajax.html?svc=token/login") {
                    body = FormDataContent(Parameters.build {
                        append("params", "{\"token\":\"${authSettings.getToken()}\",\"checkService\":\"wialon_mobile_client\",\"appName\":\"mwca.com.gurtam.wialon_local_1504.os31.v5105\"}")
                    })
                }.body<LoginResponse>()
                val sessionId = result.eid
                authSettings.saveSession(sessionId)
                authSettings.saveGisId(result.gis_sid)
                authSettings.saveName(result.au)
                authSettings.saveUserName(result.au)
                authSettings.saveId(result.user.id.toString())
                authSettings.saveAccessType(0)
                emit(Resource.Success(
                    data = LoginModel(sessionId)
                ))
            } catch (ex: Exception) {
                emit(Resource.Error(ex.message))
            }
        } else {
            emit(Resource.Error(AuthSettings.NO_TOKEN_MESSAGE))
        }
    }

    @OptIn(InternalAPI::class)
    override suspend fun logout(): Flow<Resource<Unit>> = flow {
        try {
            emit(Resource.Loading())
            httpClient.post("${Constant.BASE_URL}/wialon/ajax.html?svc=core/logout&params={}") {
                body = FormDataContent(Parameters.build {
                    append("sid", authSettings.getSessionId())
                })
            }
            emit(Resource.Success(Unit))
        } catch (ex: Exception) {
            ex.printStackTrace()
            emit(Resource.Error(ex.message))
        }
    }

}