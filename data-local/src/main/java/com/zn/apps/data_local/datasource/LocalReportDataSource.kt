package com.zn.apps.data_local.datasource

import com.zn.apps.data_local.database.report.PopulatedReportEntity
import com.zn.apps.data_local.database.report.ReportDao
import com.zn.apps.data_local.model.asEntity
import com.zn.apps.data_local.model.asExternalModel
import com.zn.apps.data_repository.data_source.local.ReportDataSource
import com.zn.apps.model.data.report.Report
import com.zn.apps.model.data.report.ReportResource
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.mapLatest
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
class LocalReportDataSource @Inject constructor(
    private val reportDao: ReportDao
): ReportDataSource {

    override fun getReportResources(): Flow<List<ReportResource>> =
        reportDao.getPopulatedReports().mapLatest {
            it.map { populatedReportEntity ->
                convertToReportResource(populatedReportEntity)
            }
        }

    override fun getReportResources(from: String, to: String): Flow<List<ReportResource>> =
        reportDao.getPopulatedReports(from, to).mapLatest {
            it.map { populatedReportEntity ->
                convertToReportResource(populatedReportEntity)
            }
        }

    private fun convertToReportResource(populatedReportEntity: PopulatedReportEntity): ReportResource {
        val reportTask =
            if (populatedReportEntity.task.isEmpty())
                null
            else
                populatedReportEntity.task[0].asExternalModel()
        val reportProject =
            if (populatedReportEntity.project.isEmpty())
                null
            else
                populatedReportEntity.project[0].asExternalModel()
        val reportTag =
            if (populatedReportEntity.tag.isEmpty())
                null
            else
                populatedReportEntity.tag[0].asExternalModel()
        return ReportResource(
            completedTime = populatedReportEntity.report.completedTime,
            elapsedTime = populatedReportEntity.report.elapsedTime,
            task = reportTask,
            project = reportProject,
            tag = reportTag
        )
    }

    override suspend fun insertReport(report: Report) {
        reportDao.insert(report.asEntity())
    }
}