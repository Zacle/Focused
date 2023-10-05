package com.zn.apps.domain.repository

import com.zn.apps.model.data.report.Report
import com.zn.apps.model.data.report.ReportResource
import kotlinx.coroutines.flow.Flow

interface ReportRepository {
    /**
     * Get all embedded reports including task, tag and project if possible
     */
    fun getReportResources(): Flow<List<ReportResource>>

    /**
     * Get embedded reports within the given interval
     */
    fun getReportResources(from: String, to: String): Flow<List<ReportResource>>

    /**
     * Insert the report
     */
    suspend fun insertReport(report: Report)
}