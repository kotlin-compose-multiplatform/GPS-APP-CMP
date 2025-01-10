package com.gs.wialonlocal.features.monitoring.data.entity.history

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.jsonObject

@Serializable
data class CustomFields(
    @SerialName("i") val i: Int? = null,
    @SerialName("d") val d: D? = null,
    @SerialName("f") val f: Long? = null
)

@Serializable
data class D(
    @SerialName("nm") val nm: String? = null,
    @SerialName("cls") val cls: Int? = null,
    @SerialName("id") val id: Int? = null,
    @SerialName("prp") val prp: Prp? = null,
    @SerialName("crt") val crt: Int? = null,
    @SerialName("bact") val bact: Int? = null,
    @SerialName("gd") val gd: String? = null,
    @SerialName("mu") val mu: Int? = null,
    @SerialName("ct") val ct: Long? = null,
    @SerialName("ftp") val ftp: Ftp? = null,
    @SerialName("pos") val pos: Pos? = null,
    @SerialName("lmsg") val lmsg: Lmsg? = null,
    @SerialName("sens") val sens: Map<String, Sens>? = null,
    @SerialName("cfl") val cfl: Int? = null,
    @SerialName("cnm") val cnm: Int? = null,
    @SerialName("cneh") val cneh: Double? = null,
    @SerialName("cnkb") val cnkb: Int? = null,
    @SerialName("prms") val prms: Map<String, JsonElement>? = null,
    @SerialName("netconn") val netconn: Int? = null,
    @SerialName("act") val act: Int? = null,
    @SerialName("dactt") val dactt: Int? = null,
    @SerialName("cnm_km") val cnmKm: Int? = null,
    @SerialName("flds") val flds: Map<String, Flds>? = null,
    @SerialName("pflds") val pflds: Map<String, Pflds>? = null,
    @SerialName("rtd") val rtd: Rtd? = null,
    @SerialName("rfc") val rfc: Rfc? = null,
    @SerialName("uri") val uri: String? = null,
    @SerialName("ugi") val ugi: Int? = null,
    @SerialName("uacl") val uacl: Long? = null,
    @SerialName("hw") val hw: Int? = null,
    @SerialName("ph") val ph: String? = null,
    @SerialName("ph2") val ph2: String? = null,
    @SerialName("uid") val uid: String? = null,
    @SerialName("uid2") val uid2: String? = null,
)

fun findParamValue(key: String,prms: Map<String, JsonElement>?, m: String): List<String> {
    val jsonParser = Json { ignoreUnknownKeys = true }

    if(key == "posinfo") {
        val json = prms?.get(key)?.let { jsonParser.decodeFromString(PrmItemPosInfo.serializer(),
            it.jsonObject.toString()
        ) }
        return listOf(
            json?.v?.x ?: 0.0,
            json?.v?.y ?: 0.0,
        ).map { it.toString() }
    } else {
        val json = prms?.get(key)?.let { jsonParser.decodeFromString(PrmItem.serializer(),
            it.jsonObject.toString()
        ) }
        if(m == "v") {
            return listOf(json?.v.toString())
        } else {
            val split = m.split("/")
            return if(split.size == 2) {
                listOf(split[json?.v?.toInt()?:0])
            } else {
                listOf(json?.v.toString())
            }
        }
    }
}

fun findParam(key: String,prms: Map<String, JsonElement>?): Pair<PrmItem?,String?> {
    val jsonParser = Json { ignoreUnknownKeys = true }

    if(key == "posinfo") {
       try {
           val json = prms?.get(key)?.let { jsonParser.decodeFromString(PrmItemPosInfo.serializer(),
               it.jsonObject.toString()
           ) }
           return Pair(null,"${json?.v?.x},${json?.v?.y}")
       } catch (ex: Exception) {
           return Pair(null,null)
       }
    } else {
       try {
           val json = prms?.get(key)?.let { jsonParser.decodeFromString(PrmItem.serializer(),
               it.jsonObject.toString()
           ) }
           return Pair(json,null)
       } catch (ex: Exception) {
           return Pair(null,null)
       }
    }
}

@Serializable
data class Prp(
    @SerialName("img_rot") val imgRot: String? = null,
    @SerialName("label_color") val labelColor: String? = null,
    @SerialName("monitoring_battery_id") val monitoringBatteryId: String? = null,
    @SerialName("monitoring_sensor") val monitoringSensor: String? = null,
    @SerialName("monitoring_sensor_id") val monitoringSensorId: String? = null,
    @SerialName("motion_state_sensor_id") val motionStateSensorId: String? = null,
    @SerialName("trip_colors") val tripColors: String? = null,
    @SerialName("use_sensor_color") val useSensorColor: String? = null
)

@Serializable
data class Ftp(
    @SerialName("ch") val ch: Int? = null,
    @SerialName("tp") val tp: Int? = null,
    @SerialName("fl") val fl: Int? = null
)

@Serializable
data class Pos(
    @SerialName("t") val t: Long? = null,
    @SerialName("f") val f: Long? = null,
    @SerialName("lc") val lc: Int? = null,
    @SerialName("y") val y: Double? = null,
    @SerialName("x") val x: Double? = null,
    @SerialName("c") val c: Int? = null,
    @SerialName("z") val z: Double? = null,
    @SerialName("s") val s: Int? = null,
    @SerialName("sc") val sc: Int? = null
)

@Serializable
data class Lmsg(
    @SerialName("t") val t: Long? = null,
    @SerialName("f") val f: Long? = null,
    @SerialName("tp") val tp: String? = null,
    @SerialName("pos") val pos: Pos? = null,
    @SerialName("i") val i: Int? = null,
    @SerialName("lc") val lc: Int? = null,
    @SerialName("rt") val rt: Long? = null,
    @SerialName("p") val p: P? = null
)

@Serializable
data class P(
    @SerialName("pwr_ext") val pwrExt: Double? = null,
    @SerialName("battery_charge") val batteryCharge: Int? = null
)

@Serializable
data class Sens(
    @SerialName("id") val id: Int? = null,
    @SerialName("n") val n: String? = null,
    @SerialName("t") val t: String? = null,
    @SerialName("d") val d: String? = null,
    @SerialName("m") val m: String? = null,
    @SerialName("p") val p: String? = null,
    @SerialName("f") val f: Int? = null,
    @SerialName("c") val c: String? = null,
    @SerialName("vt") val vt: Int? = null,
    @SerialName("vs") val vs: Int? = null,
    @SerialName("tbl") val tbl: List<Tbl>? = null,
)

@Serializable
data class Tbl(
    @SerialName("x") val x: Double? = null,
    @SerialName("a") val a: Double? = null,
    @SerialName("b") val b: Double? = null,
)


@Serializable
data class PrmItem(
    @SerialName("v") val v: Double? = null,
    @SerialName("ct") val ct: Long? = null,
    @SerialName("at") val at: Long? = null
)

@Serializable
data class PrmItemPosInfo(
    @SerialName("v") val v: PosInfo? = null,
    @SerialName("ct") val ct: Long? = null,
    @SerialName("at") val at: Long? = null
)


@Serializable
data class PosInfo(
    val y: Double,
    val x: Double,
    val z: Double,
    val c: Int,
    val sc: Int
)


@Serializable
data class Flds(
    @SerialName("n") val n: String? = null,
    @SerialName("v") val v: String? = null,
    @SerialName("ct") val ct: Long? = null,
    @SerialName("at") val at: Long? = null
)

@Serializable
data class Pflds(
    @SerialName("n") val n: String? = null,
    @SerialName("v") val v: String? = null,
    @SerialName("ct") val ct: Long? = null,
    @SerialName("at") val at: Long? = null
)

@Serializable
data class Rtd(
    @SerialName("time") val time: Long? = null,
    @SerialName("v") val v: Int? = null
)

@Serializable
data class Rfc(
    @SerialName("n") val n: String? = null,
    @SerialName("d") val d: String? = null,
    @SerialName("v") val v: Double? = null
)
