package com.gs.wialonlocal.features.auth.data.entity

import kotlinx.serialization.Serializable

@Serializable
data class LoginResponse(
    val api: String,
    val au: String,
    val base_url: String,
    val classes: Classes,
    val eid: String,
    val env: Env,
    val features: Features,
    val gis_geocode: String,
    val gis_render: String,
    val gis_routing: String,
    val gis_search: String,
    val gis_sid: String,
    val host: String,
    val hw_gw_dns: String,
    val hw_gw_ip: String,
    val local_version: String,
    val th: String,
    val tm: Int,
    val token: String,
    val user: User,
    val wsdk_version: String
)