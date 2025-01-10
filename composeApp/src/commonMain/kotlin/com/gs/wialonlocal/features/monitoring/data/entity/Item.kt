package com.gs.wialonlocal.features.monitoring.data.entity

import com.gs.wialonlocal.core.constant.Constant
import com.gs.wialonlocal.features.monitoring.data.entity.history.PrmItem
import com.gs.wialonlocal.features.monitoring.data.entity.history.Sens
import com.gs.wialonlocal.features.monitoring.data.entity.history.findParam
import com.gs.wialonlocal.features.monitoring.domain.model.UnitModel
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.jsonObject

fun convertTimestampToHours(unixTimestamp: Long): Int {
    val timeZone = TimeZone.of("Asia/Ashgabat")
    val currentDate = Clock.System.now().toLocalDateTime(timeZone)

    // Convert lastOnlineTime (assuming it's a timestamp in seconds) to Instant and adjust to the same timezone
    val pastDate = try {
        Instant.fromEpochSeconds(unixTimestamp).toLocalDateTime(timeZone)
    } catch (ex: Exception) {
        Clock.System.now().toLocalDateTime(timeZone)
    }

    // Calculate the duration between currentDate and pastDate
    val duration = currentDate.toInstant(timeZone) - pastDate.toInstant(timeZone)



    return duration.inWholeHours.toInt()
}

@Serializable
data class Item(
    val cls: Int? = null,
    val id: Int = 0,
    val lmsg: Lmsg? = null,
    val mu: Int? = null,
    val nm: String? = null,
    val pos: PosX? = null,
    val uacl: Long? = null,
    val ugi: Int? = null,
    val uri: String? = null,
    val netconn: Int? = 0,
    val rtd: TripDetector? = null,
    @SerialName("sens") val sens: Map<String, Sens>? = null,
    @SerialName("prms") val prms: Map<String, JsonElement>? = null,
) {

    fun ignitionOn(): Boolean {
        sens?.map {
            val engine = sens.values.find { it.t == "engine operation" }
            val prm = findParam(engine?.p ?: "in1", prms)
//            val res = engine?.m?.split("/")
//            if(res.isNullOrEmpty())
//                return false

            println("IGNITION: ${prm.first?.v}, ${prm.second}")
            prm.first?.v?.let { v ->
                return v.toInt() != 0
            }
            return false
        }
        return false
    }

    fun isStationary(): Boolean {
        return if (lmsg?.pos?.s != null) lmsg.pos.s < minMovingSpeed() else false
    }

    fun minMovingSpeed(): Double {
        rtd?.minMovingSpeed?.let {
            return it
        }
        return 0.0
    }

    fun getLocation(): String {
        return "{\"lon\":${pos?.x ?: 0},\"lat\":${pos?.y ?: 0}}"
    }

    fun toUiEntity(address: String): UnitModel {
        println("Image: ${Constant.BASE_URL}${uri}")
        return UnitModel(
            id = id.toString(),
            carNumber = nm?:"",
            longitude = pos?.x ?: 0.0,
            latitude = pos?.y ?: 0.0,
            estimateTime = pos?.t.toString(),
            estimateDistance = "",
            image = "${Constant.BASE_URL}${uri}",
            speed = pos?.s.toString().plus(" km/h"),
            number = nm?:"",
            lastOnlineTime = lmsg?.t.toString(),
            isOnline = netconn == 1 || (lmsg?.t!=null && convertTimestampToHours(lmsg.t) < 1),
            address = address,
            moving = if (lmsg?.pos?.s != null) lmsg.pos.s >= minMovingSpeed() else false,
            stationary = isStationary() && ignitionOn().not(),
            stationaryWithIgnitionOn = ignitionOn() && isStationary(),
            noActualState = lmsg?.t!=null && convertTimestampToHours(lmsg.t) >= 1,
            noMessages = lmsg==null,
            ignitionOn = ignitionOn() //&& (lmsg?.t!=null && convertTimestampToHours(lmsg.t) < 1)
        )
    }
}