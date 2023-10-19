package com.zn.apps.domain.task

import com.zn.apps.domain.UseCase
import com.zn.apps.model.data.report.CalendarReportType
import com.zn.apps.model.data.report.StatsReport
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import java.time.OffsetDateTime

class GetTaskStatsUseCase(
    configuration: Configuration,
    private val getTasksStatsOverviewUseCase: GetTasksStatsOverviewUseCase,
    private val getTaskHistogramStatsUseCase: GetTaskHistogramStatsUseCase
): UseCase<GetTaskStatsUseCase.Request, GetTaskStatsUseCase.Response>(configuration) {

    override suspend fun process(request: Request): Flow<Response> =
        combine(
            getTasksStatsOverviewUseCase.process(
                GetTasksStatsOverviewUseCase.Request
            ),
            getTaskHistogramStatsUseCase.process(
                GetTaskHistogramStatsUseCase.Request(
                    calendarReportType = request.calendarReportType,
                    dateTime = request.dateTime
                )
            )
        ) { taskStatsOverviewResponse, taskHistogramStatsResponse ->
            Response(
                totalHours = taskStatsOverviewResponse.totalHoursSpent,
                totalCompleted = taskStatsOverviewResponse.tasksCompleted,
                statsReport = taskHistogramStatsResponse.statsReport
            )
        }

    data class Request(
        val calendarReportType: CalendarReportType,
        val dateTime: OffsetDateTime
    ): UseCase.Request

    data class Response(
        val totalHours: Int,
        val totalCompleted: Int,
        val statsReport: StatsReport
    ): UseCase.Response
}