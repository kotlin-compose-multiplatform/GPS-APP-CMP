package com.gs.wialonlocal.features.report.data.repository

import com.gs.wialonlocal.core.constant.Constant
import com.gs.wialonlocal.core.network.Resource
import com.gs.wialonlocal.features.auth.data.AuthSettings
import com.gs.wialonlocal.features.monitoring.domain.model.UnitModel
import com.gs.wialonlocal.features.report.data.entity.template.GetTemplateItem
import com.gs.wialonlocal.features.report.domain.model.TemplateModel
import com.gs.wialonlocal.features.report.domain.repository.ReportRepository
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.forms.FormDataContent
import io.ktor.client.request.post
import io.ktor.http.Parameters
import io.ktor.util.InternalAPI
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class ReportRepositoryImpl(
    private val httpClient: HttpClient,
    private val authSettings: AuthSettings
): ReportRepository {
    @OptIn(InternalAPI::class)
    override suspend fun getTemplates(): Flow<Resource<List<TemplateModel>>> = flow {
        try {
            emit(Resource.Loading())
            httpClient.post("${Constant.BASE_URL}/wialon/ajax.html?svc=core/update_data_flags") {
                body = FormDataContent(Parameters.build {
                    append("sid", authSettings.getSessionId())
                    append("params", "{\"spec\":[{\"type\":\"type\",\"max_items\": -1,\"data\": \"avl_resource\",\"mode\": 2,\"flags\": 8449}]}")
                })
            }
            val templates = httpClient.post("${Constant.BASE_URL}/wialon/ajax.html?svc=core/update_data_flags") {
                body = FormDataContent(Parameters.build {
                    append("sid", authSettings.getSessionId())
                    append("params", "{\"spec\":[{\"type\":\"type\",\"max_items\": -1,\"data\": \"avl_resource\",\"mode\": 1,\"flags\": 8449}]}")
                })
            }.body<List<GetTemplateItem>>()
            val res = templates.find { !it.d.rep.isNullOrEmpty() }
            println("________TEMPLATE___________")
            println(res)
            println("________TEMPLATE___________")
            if(res!=null) {
                emit(Resource.Success(
                    data = res.d.rep?.values?.map { tmp->
                        TemplateModel(
                            id = tmp.id.toString(),
                            name = tmp.n,
                            templateCt = tmp.ct,
                            templateC = tmp.c,
                            uacl = res.d.uacl?:0,
                            resourceD = res.d.id,
                            resourceId = res.i
                        )
                    }
                ))
            }

        } catch (ex: Exception) {
            ex.printStackTrace()
            emit(Resource.Error(ex.message))
        }
    }

    @OptIn(InternalAPI::class)
    override suspend fun executeReport(
        templateModel: TemplateModel,
        unitModel: UnitModel,
        startDate: Long,
        endDate: Long,
        orientation: String
    ): Flow<Resource<ByteArray>> = flow {
        emit(Resource.Loading())
        try {
            httpClient.post("${Constant.BASE_URL}/wialon/ajax.html?svc=report/cleanup_result") {
                body = FormDataContent(Parameters.build {
                    append("sid", authSettings.getSessionId())
                    append("params", "{}")
                })
            }

            httpClient.post("${Constant.BASE_URL}/wialon/ajax.html?svc=report/exec_report") {
                body = FormDataContent(Parameters.build {
                    append("sid", authSettings.getSessionId())
                    append("params", "{\"reportResourceId\":${templateModel.resourceId},\"reportTemplateId\":${templateModel.id},\"reportObjectId\":8049,\"reportObjectSecId\":0,\"tzOffset\":18000,\"language\":\"en\",\"reportTemplate\":null,\"interval\":{\"from\":${startDate},\"to\":${endDate},\"flags\":0}}")
                })
            }

            val pdf = httpClient.post("${Constant.BASE_URL}/wialon/ajax.html?svc=report/export_result") {
                body = FormDataContent(Parameters.build {
                    append("sid", authSettings.getSessionId())
                    append("params", "{\"format\":2,\"compress\":\"0\",\"pageOrientation\":\"${orientation}\",\"pageSize\":\"a4\",\"pageWidth\":\"0\",\"attachMap\":\"1\",\"extendBounds\":\"0\",\"hideMapBasis\":\"0\",\"outputFileName\":\"Online_report\"}")
                })
            }.body<ByteArray>()

            println("PDFFFFFFFFFFFFFF"+pdf.size)

            emit(Resource.Success(pdf))

        } catch (ex: Exception) {
            ex.printStackTrace()
            emit(Resource.Error(ex.message))
        }
    }
}