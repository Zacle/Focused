package com.zn.apps.domain.task

import com.zn.apps.domain.UseCase
import com.zn.apps.domain.repository.TaskRepository
import com.zn.apps.domain.util.formatToString
import com.zn.apps.domain.util.report.CalendarReport
import com.zn.apps.model.data.report.CalendarReportType
import com.zn.apps.model.data.report.StatsReport
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.OffsetDateTime

class GetTaskHistogramStatsUseCase(
    configuration: Configuration,
    private val taskRepository: TaskRepository
): UseCase<GetTaskHistogramStatsUseCase.Request, GetTaskHistogramStatsUseCase.Response>(configuration) {

    override suspend fun process(request: Request): Flow<Response> {
        val (calendarReportType, dateTime) = request
        val calendarReport = CalendarReport.getCalendarReport(
            calendarReportType = calendarReportType,
            currentDay = dateTime
        )
        val reportInterval = calendarReport.getReportInterval()
        return taskRepository.getTaskResources(
            from = reportInterval.startTime.formatToString(),
            to = reportInterval.endTime.formatToString()
        ).map { taskResources ->
            Response(calendarReport.getTaskStats(taskResources))
        }
    }

    data class Request(
        val calendarReportType: CalendarReportType,
        val dateTime: OffsetDateTime
    ): UseCase.Request

    data class Response(val statsReport: StatsReport): UseCase.Response
}