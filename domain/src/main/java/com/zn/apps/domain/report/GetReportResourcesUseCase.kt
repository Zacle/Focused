package com.zn.apps.domain.report

import com.zn.apps.domain.UseCase
import com.zn.apps.domain.repository.ReportRepository
import com.zn.apps.model.data.report.ReportResource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetReportResourcesUseCase(
    configuration: Configuration,
    private val reportRepository: ReportRepository
): UseCase<GetReportResourcesUseCase.Request, GetReportResourcesUseCase.Response>(configuration) {

    override suspend fun process(request: Request): Flow<Response> =
        reportRepository.getReportResources().map {
            Response(it)
        }

    data object Request: UseCase.Request

    data class Response(val reportResources: List<ReportResource>): UseCase.Response
}