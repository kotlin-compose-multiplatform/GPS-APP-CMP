package com.gs.wialonlocal.features.geofence.data.repository

import com.gs.wialonlocal.core.constant.Constant
import com.gs.wialonlocal.core.network.Resource
import com.gs.wialonlocal.features.auth.data.AuthSettings
import com.gs.wialonlocal.features.geofence.data.entity.GeofenceApiItem
import com.gs.wialonlocal.features.geofence.data.entity.geofence.RealGeofenceApiItem
import com.gs.wialonlocal.features.geofence.domain.repository.GeofenceRepository
import com.gs.wialonlocal.features.report.data.entity.template.GetTemplateItem
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.forms.FormDataContent
import io.ktor.client.request.post
import io.ktor.http.Parameters
import io.ktor.util.InternalAPI
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class GeofenceRepositoryImpl(
    private val httpClient: HttpClient,
    private val authSettings: AuthSettings
) : GeofenceRepository {
    @OptIn(InternalAPI::class)
    override suspend fun getGeofence(): Flow<Resource<List<RealGeofenceApiItem>>> = flow {
        emit(Resource.Loading())
        try {
            httpClient.post("${Constant.BASE_URL}/wialon/ajax.html?svc=core/update_data_flags") {
                body = FormDataContent(Parameters.build {
                    append("sid", authSettings.getSessionId())
                    append("params", "{\"spec\":[{\"type\":\"type\",\"max_items\":-1,\"data\":\"avl_resource\",\"mode\":2,\"flags\":1052672}]}")
                })
            }
            val geofences = httpClient.post("${Constant.BASE_URL}/wialon/ajax.html?svc=core/update_data_flags") {
                body = FormDataContent(Parameters.build {
                    append("sid", authSettings.getSessionId())
                    append("params", "{\"spec\":[{\"type\":\"type\",\"max_items\":-1,\"data\":\"avl_resource\",\"mode\":1,\"flags\":1052672}]}")
                })
            }.body<List<GeofenceApiItem>>()
            val res = geofences.find { it.d.zl.isNotEmpty() }
            val response = httpClient.post("${Constant.BASE_URL}/wialon/ajax.html?svc=resource/get_zone_data") {
                body = FormDataContent(Parameters.build {
                    append("params", "{\"itemId\":${res?.i},\"flags\":25}")
                    append("sid", authSettings.getSessionId())
                })
            }
            if(response.status.value in 200..300) {
                val geofence = response.body<List<RealGeofenceApiItem>>()
                emit(Resource.Success(geofence))
            } else {
                emit(Resource.Error(response.status.description, response.status.value))
            }
        } catch (ex: Exception) {
            ex.printStackTrace()
            emit(Resource.Error(ex.message))
        }
    }
}