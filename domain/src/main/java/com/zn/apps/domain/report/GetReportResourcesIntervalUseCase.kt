package com.zn.apps.domain.report

import com.zn.apps.domain.UseCase
import com.zn.apps.domain.repository.ReportRepository
import com.zn.apps.domain.util.formatToString
import com.zn.apps.model.data.report.ReportResource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.OffsetDateTime

class GetReportResourcesIntervalUseCase(
    configuration: Configuration,
    private val reportRepository: ReportRepository
): UseCase<GetReportResourcesIntervalUseCase.Request, GetReportResourcesIntervalUseCase.Response>(configuration) {

    override suspend fun process(request: Request): Flow<Response> =
        reportRepository.getReportResources(
            from = request.from.formatToString(),
            to = request.to.formatToString()
        ).map {
            Response(it)
        }

    data class Request(val from: OffsetDateTime, val to: OffsetDateTime): UseCase.Request

    data class Response(val reportResources: List<ReportResource>): UseCase.Response
}