package com.gs.wialonlocal.features.global.data.repository

import com.gs.wialonlocal.core.constant.Constant
import com.gs.wialonlocal.core.network.Resource
import com.gs.wialonlocal.features.auth.data.AuthSettings
import com.gs.wialonlocal.features.global.data.entity.CheckSessionEntity
import com.gs.wialonlocal.features.global.data.entity.GetVersionsItem
import com.gs.wialonlocal.features.global.domain.model.CheckSessionModel
import com.gs.wialonlocal.features.global.domain.repository.GlobalRepository
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class GlobalRepositoryImpl(
    private val authSettings: AuthSettings,
    private val httpClient: HttpClient
): GlobalRepository {
    override suspend fun checkSession(): Flow<Resource<CheckSessionModel>> = flow {
        if(authSettings.isSessionIdExist()) {
            emit(Resource.Loading())
            try {
                val result = httpClient.get("${Constant.BASE_URL}/avl_evts?sid=${authSettings.getSessionId()}").body<CheckSessionEntity>()
                emit(Resource.Success(CheckSessionModel(error = result.tm > 0)))
            } catch (ex: Exception) {
                emit(Resource.Error(ex.message))
            }

        } else {
            emit(Resource.Error(AuthSettings.NO_SESSION_MESSAGE))
        }
    }

    override suspend fun getVersions(): Flow<Resource<List<GetVersionsItem>>> = flow {
        emit(Resource.Loading())
        try {
            val response = httpClient.get("https://geekspace.dev/wialon-version")
            if(response.status.value in 200..299) {
                emit(Resource.Success(response.body()))
            } else {
                emit(Resource.Error(response.status.description, response.status.value))
            }
        } catch (ex: Exception) {
            ex.printStackTrace()
            emit(Resource.Error(ex.message))
        }
    }


}