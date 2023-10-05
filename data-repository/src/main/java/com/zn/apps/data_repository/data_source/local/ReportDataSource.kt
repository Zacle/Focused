package com.zn.apps.data_repository.data_source.local

import com.zn.apps.model.data.report.Report
import com.zn.apps.model.data.report.ReportResource
import kotlinx.coroutines.flow.Flow

interface ReportDataSource {
    /**
     * Get report resources and embedding related data
     */
    fun getReportResources(): Flow<List<ReportResource>>

    /**
     * Get report resources with the given interval
     */
    fun getReportResources(from: String, to: String): Flow<List<ReportResource>>

    /**
     * Insert report
     */
    suspend fun insertReport(report: Report)
}