package com.gs.wialonlocal.features.report.domain.repository

import com.gs.wialonlocal.core.network.Resource
import com.gs.wialonlocal.features.monitoring.domain.model.UnitModel
import com.gs.wialonlocal.features.report.domain.model.TemplateModel
import kotlinx.coroutines.flow.Flow

interface ReportRepository {
    suspend fun getTemplates(): Flow<Resource<List<TemplateModel>>>
    suspend fun executeReport(
        templateModel: TemplateModel,
        unitModel: UnitModel,
        startDate: Long,
        endDate: Long,
        orientation: String
    ): Flow<Resource<ByteArray>>
}