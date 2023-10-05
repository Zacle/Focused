package com.zn.apps.data_repository.repository

import com.zn.apps.data_repository.data_source.local.ReportDataSource
import com.zn.apps.domain.repository.ReportRepository
import com.zn.apps.model.data.report.Report
import com.zn.apps.model.data.report.ReportResource
import kotlinx.coroutines.flow.Flow

class DefaultReportRepository(
    private val reportDataSource: ReportDataSource
): ReportRepository {

    override fun getReportResources(): Flow<List<ReportResource>> =
        reportDataSource.getReportResources()

    override fun getReportResources(from: String, to: String): Flow<List<ReportResource>> =
        reportDataSource.getReportResources(from, to)

    override suspend fun insertReport(report: Report) {
        reportDataSource.insertReport(report)
    }
}