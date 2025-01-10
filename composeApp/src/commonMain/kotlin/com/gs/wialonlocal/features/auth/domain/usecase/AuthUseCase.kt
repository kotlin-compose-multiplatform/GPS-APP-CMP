package com.gs.wialonlocal.features.auth.domain.usecase

import com.gs.wialonlocal.core.network.Resource
import com.gs.wialonlocal.features.auth.domain.model.LoginModel
import com.gs.wialonlocal.features.auth.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow

class AuthUseCase(
    private val repository: AuthRepository
) {
    suspend fun login(): Flow<Resource<LoginModel>> {
        return repository.login()
    }

    suspend fun logout(): Flow<Resource<Unit>> {
        return repository.logout()
    }
}