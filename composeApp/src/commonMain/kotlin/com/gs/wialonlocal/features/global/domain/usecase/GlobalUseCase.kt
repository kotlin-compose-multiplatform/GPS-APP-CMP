package com.gs.wialonlocal.features.global.domain.usecase

import com.gs.wialonlocal.core.network.Resource
import com.gs.wialonlocal.features.global.data.entity.GetVersionsItem
import com.gs.wialonlocal.features.global.domain.model.CheckSessionModel
import com.gs.wialonlocal.features.global.domain.repository.GlobalRepository
import kotlinx.coroutines.flow.Flow

class GlobalUseCase(
    private val repository: GlobalRepository
) {
    suspend fun checkSession(): Flow<Resource<CheckSessionModel>> {
        return repository.checkSession()
    }
    suspend fun getVersions(): Flow<Resource<List<GetVersionsItem>>> {
        return repository.getVersions()
    }
}