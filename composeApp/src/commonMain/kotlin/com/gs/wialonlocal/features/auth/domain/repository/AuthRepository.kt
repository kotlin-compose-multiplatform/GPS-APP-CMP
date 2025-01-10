package com.gs.wialonlocal.features.auth.domain.repository

import com.gs.wialonlocal.core.network.Resource
import com.gs.wialonlocal.features.auth.domain.model.LoginModel
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    suspend fun login(): Flow<Resource<LoginModel>>
    suspend fun logout(): Flow<Resource<Unit>>
}