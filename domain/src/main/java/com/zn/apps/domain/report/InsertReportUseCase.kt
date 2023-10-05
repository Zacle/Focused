package com.zn.apps.domain.report

import com.zn.apps.domain.UseCase
import com.zn.apps.domain.repository.ReportRepository
import com.zn.apps.model.data.report.Report
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class InsertReportUseCase(
    configuration: Configuration,
    private val reportRepository: ReportRepository
): UseCase<InsertReportUseCase.Request, InsertReportUseCase.Response>(configuration) {

    override suspend fun process(request: Request): Flow<Response> {
        reportRepository.insertReport(request.report)
        return flowOf(Response)
    }

    data class Request(val report: Report): UseCase.Request

    data object Response: UseCase.Response
}