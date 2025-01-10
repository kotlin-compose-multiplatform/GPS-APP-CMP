package com.gs.wialonlocal.features.monitoring.data.repository

import com.gs.wialonlocal.common.LatLong
import com.gs.wialonlocal.core.constant.Constant
import com.gs.wialonlocal.core.network.Resource
import com.gs.wialonlocal.features.auth.data.AuthSettings
import com.gs.wialonlocal.features.monitoring.data.entity.GetUnits
import com.gs.wialonlocal.features.monitoring.data.entity.TripDetector
import com.gs.wialonlocal.features.monitoring.data.entity.hardware.HardwareTypeEntity
import com.gs.wialonlocal.features.monitoring.data.entity.history.CustomFields
import com.gs.wialonlocal.features.monitoring.data.entity.history.GetEvents
import com.gs.wialonlocal.features.monitoring.data.entity.history.GetReportSettings
import com.gs.wialonlocal.features.monitoring.data.entity.history.LoadEventRequest
import com.gs.wialonlocal.features.monitoring.data.entity.history.Trip
import com.gs.wialonlocal.features.monitoring.data.entity.history.categorizeAndMergeTrips
import com.gs.wialonlocal.features.monitoring.data.entity.locator.LocatorResponse
import com.gs.wialonlocal.features.monitoring.data.entity.updates.Data
import com.gs.wialonlocal.features.monitoring.data.entity.updates.Position
import com.gs.wialonlocal.features.monitoring.data.settings.UnitsSettings
import com.gs.wialonlocal.features.monitoring.domain.model.UnitModel
import com.gs.wialonlocal.features.monitoring.domain.repository.MonitoringRepository
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.forms.FormDataContent
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.statement.bodyAsText
import io.ktor.http.Parameters
import io.ktor.util.InternalAPI
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject

private val json = Json {
    ignoreUnknownKeys = true
}

class MonitoringRepositoryImpl(
    private val authSettings: AuthSettings,
    private val httpClient: HttpClient,
    private val unitsSettings: UnitsSettings
) : MonitoringRepository {
    @OptIn(InternalAPI::class)
    override suspend fun getEvents(isAddressRequired: Boolean): Flow<Resource<List<UnitModel>>> = flow {
        emit(Resource.Loading())
        try {
            var selectedIds = unitsSettings.getUnits()
            val result =
                httpClient.post("${Constant.BASE_URL}/wialon/ajax.html?svc=core/search_items") {
                    body = FormDataContent(Parameters.build {
                        append("sid", authSettings.getSessionId())
                        append(
                            "params",
                            "{\"spec\":{\"itemsType\":\"avl_unit\",\"sortType\":\"sys_name\",\"propName\":\"sys_name\",\"propValueMask\":\"*\"}, \"force\":1, \"flags\":4611686018427387903, \"from\":0, \"to\":0}"
                        )
                    })
                }.body<GetUnits>()
            if(selectedIds.isEmpty()) {
                unitsSettings.saveUnits(result.items?.map { it.id.toString() }?: emptyList())
            }
            selectedIds = unitsSettings.getUnits()
            println(selectedIds)
            addUnitsToUpdate(selectedIds.map { it.toLong() })

            var address: List<String> = emptyList()

//            if(isAddressRequired) {
//                result.items?.forEach { item->
//
//                }
//            } else {
                result.items?.forEach { _ ->
                    address = address.plus("")
                }
//            }



            emit(Resource.Success(
                data = result.items?.mapIndexed { index, item ->
                    item.toUiEntity(address[index])
                }
            ))
        } catch (ex: Exception) {
            ex.printStackTrace()
            emit(Resource.Error(ex.message, 500))
        }
    }

    @OptIn(InternalAPI::class)
    override suspend fun getTripDetector(): Flow<Resource<TripDetector>> = flow {
        val tripDetector = httpClient.post("${Constant.BASE_URL}/wialon/ajax.html?svc=unit/get_trip_detector") {
            body = FormDataContent(Parameters.build {
                append("sid", authSettings.getSessionId())
                append("params", "{\"itemId\":118}")
            })
        }.body<TripDetector>()
    }

    @OptIn(InternalAPI::class)
    suspend fun addUnitsToUpdate(ids: List<Long>) {
        try {
            val units = ids.joinToString(",") { "{\"id\":${it},\"detect\":{\"*\":0}}" }
            val result =
                httpClient.post("https://gps.ytm.tm/wialon/ajax.html?svc=events/update_units") {
                    body = FormDataContent(Parameters.build {
                        append("sid", authSettings.getSessionId())
                        append("params", "{\"mode\":\"add\", \"units\":[${units}]}")
                    })
                }.bodyAsText()
            println("Add units: $result")

        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }

    fun getCurrentUnixTimestampWithTimeZone(): Long {
        val timeZone = TimeZone.of("Asia/Ashgabat")
        val currentDateTime = Clock.System.now().toLocalDateTime(timeZone)
        // Convert the current DateTime in the specified time zone back to an Instant
        val instantInTimeZone = currentDateTime.toInstant(timeZone)
        return instantInTimeZone.epochSeconds
    }

    @OptIn(InternalAPI::class)
    override suspend fun getUpdates(oldEvents: List<UnitModel>): Flow<Resource<List<UnitModel>>> =
        flow {
            emit(Resource.Loading())
            var updated = oldEvents
            try {
                val jsonResponse = httpClient.post("${Constant.BASE_URL}/wialon/ajax.html?svc=events/check_updates") {
                        body = FormDataContent(Parameters.build {
                            append("sid", authSettings.getSessionId())
                            append("params", "{\"lang\":\"tk\",\"measure\":0,\"detalization\":55}")
                        })//12273
                    }.body<JsonObject>()
                if (jsonResponse.isEmpty()) {
                    println("Response is empty")
                    emit(Resource.Error("Response is empty"))
                } else {
                    jsonResponse.entries.forEach { entry ->
                        val dynamicKey = entry.key
                        val dataArray = entry.value.jsonArray
                        val data = dataArray[0].jsonObject

                        val decode = json.decodeFromString<Data>(data.toString())
                        try {
                            if(decode.ignition!=null) {
                                val ignition = decode.ignition.values.toList()[0]?.state?:0
                                updated = updated.map {
                                    if(it.id == dynamicKey) {
                                        it.copy(
                                            ignitionOn = ignition!=0
                                        )
                                    } else {
                                        it
                                    }
                                }
                            }
                        } catch (ex: Exception) {
                            ex.printStackTrace()
                        }
                        if (decode.trips != null) {
                            val newPosition = decode.trips.to ?: Position(0, 0.0, 0.0)
                            updated = updated.map {
                                if(it.id == dynamicKey) {
                                    it.copy(
                                        latitude = newPosition.y,
                                        longitude = newPosition.x,
                                        trips = it.trips.plus(
                                            LatLong(newPosition.y, newPosition.x)
                                        ),
                                        lastOnlineTime = decode.trips.to?.t.toString()
                                    )
                                } else {
                                    it
                                }
                            }
                        }
                        println("UPDATE: $decode")
                    }
                    emit(Resource.Success(updated))
                    // val address = httpClient.get("${Constant.BASE_URL}/gis_geocode?coords=[${locations}]&flags=1255211008&uid=${authSettings.getId()}").body<List<String>>()

                }

            } catch (ex: Exception) {
                ex.printStackTrace()
                emit(Resource.Error(ex.message))
            }
        }

    @OptIn(InternalAPI::class)
    override suspend fun getReportSettings(itemId: String): Flow<Resource<GetReportSettings>> = flow {
        emit(Resource.Loading())
        try {
            val result = httpClient.post("${Constant.BASE_URL}/wialon/ajax.html?svc=unit/get_report_settings") {
                body = FormDataContent(Parameters.build {
                    append("sid", authSettings.getSessionId())
                    append("params", "{\"itemId\":${itemId}}")
                })
            }.body<GetReportSettings>()
            emit(Resource.Success(result))
        } catch (ex: Exception) {
            ex.printStackTrace()
            emit(Resource.Error(ex.message))
        }
    }

    @OptIn(InternalAPI::class)
    override suspend fun loadEvents(req: LoadEventRequest): Flow<Resource<Pair<List<Trip>, List<Trip>>>> = flow {
        emit(Resource.Loading())
        try {
            val tripDetector = httpClient.post("${Constant.BASE_URL}/wialon/ajax.html?svc=unit/get_trip_detector") {
                body = FormDataContent(Parameters.build {
                    append("sid", authSettings.getSessionId())
                    append("params", "{\"itemId\":${req.itemId}}")
                })
            }.body<TripDetector>()
            println("____________DETECTOR__________")
            println(tripDetector)
            println("____________DETECTOR__________")
            val result = httpClient.post("${Constant.BASE_URL}/wialon/ajax.html?svc=unit/get_events") {
                body = FormDataContent(Parameters.build {
                    append("sid", authSettings.getSessionId())
                    append("params", "{\"itemId\":${req.itemId},\"eventType\":\"trips\",\"ivalType\":1,\"ivalFrom\":${req.timeFrom},\"ivalTo\":${req.timeTo},\"detalization\":47}")
                })
            }.body<GetEvents>()
            val categorized = categorizeAndMergeTrips(result.trips, tripDetector)
                .map {
                    if(it.type == "trip"){
                        it
                    } else {
                        try {
                            val address =
                                httpClient.get("${Constant.BASE_URL}/gis_geocode?coords=[{\"lon\":${it.from.x},\"lat\":${it.from.y}}]&flags=1255211008&uid=${authSettings.getId()}")
                                    .body<List<String>>()
                            it.copy(
                                address = address[0]
                            )
                        } catch (ex: Exception) {
                            it
                        }
                    }
                }
            if(result.trips.isNullOrEmpty()) {
                emit(Resource.Error("No trips found"))
            } else {
                emit(Resource.Success(Pair(result.trips, categorized)))
            }
        } catch (ex: Exception) {
            ex.printStackTrace()
            emit(Resource.Error(ex.message))
        }
    }

    @OptIn(InternalAPI::class)
    override suspend fun unloadEvents(id: String): Flow<Resource<Unit>> = flow {
        emit(Resource.Loading())
        try {
            httpClient.post("${Constant.BASE_URL}/wialon/ajax.html?svc=core/batch") {
                body = FormDataContent(Parameters.build {
                    append("sid", authSettings.getSessionId())
                    append("params", "[{\"svc\":\"core/update_data_flags\",\"params\":{\"spec\":[{\"type\":\"col\",\"max_items\":-1,\"data\":[${id}],\"mode\":2,\"flags\":4611686018427387903}]}}]")
                })
            }
            httpClient.post("${Constant.BASE_URL}/wialon/ajax.html?svc=/events/unload") {
                body = FormDataContent(Parameters.build {
                    append("sid", authSettings.getSessionId())
                    append("params", "{}")
                })
            }


            emit(Resource.Success(Unit))
        } catch (ex: Exception) {
            ex.printStackTrace()
            emit(Resource.Error(ex.message))
        }
    }


    @OptIn(InternalAPI::class)
    override suspend fun getEvent(id: String, mode: Int): Flow<Resource<CustomFields>> = flow {
        emit(Resource.Loading())
        try {
            val result = httpClient.post("${Constant.BASE_URL}/wialon/ajax.html?svc=core/batch") {
                body = FormDataContent(Parameters.build {
                    append("sid", authSettings.getSessionId())
                    append("params", "[{\"svc\":\"core/update_data_flags\",\"params\":{\"spec\":[{\"type\":\"col\",\"max_items\":-1,\"data\":[${id}],\"mode\":${mode},\"flags\":4611686018427387903}]}}]")
                })
            }.body<List<List<CustomFields>>>()

            emit(Resource.Success(result[0][0]))
        } catch (ex: Exception) {
            ex.printStackTrace()
            emit(Resource.Error(ex.message))
        }
    }

    @OptIn(InternalAPI::class)
    override suspend fun getLocatorUrl(
        duration: Long,
        items: List<String>
    ): Flow<Resource<LocatorResponse>> = flow {
        emit(Resource.Loading())
        try {
            val params = "{\"callMode\":\"create\",\"app\":\"locator\",\"at\":0,\"dur\":${duration},\"fl\":256,\"p\":\"{\\\"note\\\":\\\"\\\",\\\"zones\\\":0,\\\"tracks\\\":0}\",\"items\":[${
                items.joinToString(
                    ","
                )
            }]}"
            println(params)
            val result = httpClient.post("${Constant.BASE_URL}/wialon/ajax.html?svc=token/update") {
                body = FormDataContent(Parameters.build {
                    append("params", params)
                    append("sid", authSettings.getSessionId())
                })
            }.body<LocatorResponse>()
            emit(Resource.Success(result))
        } catch (ex: Exception) {
            ex.printStackTrace()
            emit(Resource.Error(ex.message))
        }
    }

    @OptIn(InternalAPI::class)
    override suspend fun getHardwareTypes(): Flow<Resource<List<HardwareTypeEntity>>> = flow {
        emit(Resource.Loading())
        try {
            val response =  httpClient.post("${Constant.BASE_URL}/wialon/ajax.html?svc=core/get_hw_types") {
                body = FormDataContent(Parameters.build {
                    append("sid", authSettings.getSessionId())
                    append("params", "{}")
                })
            }

            if(response.status.value in 200..299) {
                emit(Resource.Success(response.body()))
            } else {
                emit(Resource.Error(response.status.description, response.status.value))
            }
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }

    override suspend fun getAddress(latitude: Double, longitude: Double): Flow<Resource<String>> = flow {
        try {
            emit(Resource.Loading())
            val location = "{\"lon\":${longitude ?: 0},\"lat\":${latitude ?: 0}}"
            val addressResult =
                httpClient.get("${Constant.BASE_URL}/gis_geocode?coords=[${location}]&flags=1255211008&uid=${authSettings.getId()}")
                    .body<List<String>>()
            emit(Resource.Success(addressResult.firstOrNull()?:""))
        } catch (ex: Exception) {
            ex.printStackTrace()
            emit(Resource.Success(""))
        }
    }

}