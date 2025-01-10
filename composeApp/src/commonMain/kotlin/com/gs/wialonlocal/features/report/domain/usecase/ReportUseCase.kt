package com.gs.wialonlocal.features.report.domain.usecase

import com.gs.wialonlocal.core.network.Resource
import com.gs.wialonlocal.features.monitoring.domain.model.UnitModel
import com.gs.wialonlocal.features.report.domain.model.TemplateModel
import com.gs.wialonlocal.features.report.domain.repository.ReportRepository
import kotlinx.coroutines.flow.Flow

class ReportUseCase(
    private val repository: ReportRepository
) {
    suspend fun getTemplates(): Flow<Resource<List<TemplateModel>>> {
        return repository.getTemplates()
    }

    suspend fun executeReport(
        templateModel: TemplateModel,
        unitModel: UnitModel,
        startDate: Long,
        endDate: Long,
        orientation: String
    ): Flow<Resource<ByteArray>> {
        return repository.executeReport(
            templateModel, unitModel, startDate, endDate, orientation
        )
    }
}