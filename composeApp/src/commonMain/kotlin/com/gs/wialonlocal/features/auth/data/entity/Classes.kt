package com.gs.wialonlocal.features.auth.data.entity

import kotlinx.serialization.Serializable

@Serializable
data class Classes(
    val avl_hw: Int,
    val avl_resource: Int,
    val avl_retranslator: Int,
    val avl_route: Int,
    val avl_unit: Int,
    val avl_unit_group: Int,
    val user: Int
)