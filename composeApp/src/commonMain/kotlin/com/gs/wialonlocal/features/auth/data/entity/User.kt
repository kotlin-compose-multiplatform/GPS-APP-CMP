package com.gs.wialonlocal.features.auth.data.entity

import kotlinx.serialization.Serializable

@Serializable
data class User(
    val bact: Int? = null,
    val cls: Int? = null,
    val crt: Int? = null,
    val ct: Int? = null,
    val fl: Int? = null,
    val ftp: Ftp? = null,
    val hm: String? = null,
    val id: Int? = null,
    val ld: Int? = null,
    val mappsmax: Int? = null,
    val mu: Int? = null,
    val nm: String? = null,
    val pfl: Int? = null,
    val prp: Prp? = null,
    val uacl: Int? = null
)