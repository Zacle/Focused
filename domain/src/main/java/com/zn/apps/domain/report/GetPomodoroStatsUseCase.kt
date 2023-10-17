package com.zn.apps.domain.report

import com.zn.apps.domain.UseCase
import com.zn.apps.model.data.report.CalendarReportType
import com.zn.apps.model.data.report.PomodoroStatsReport
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import java.time.OffsetDateTime

class GetPomodoroStatsUseCase(
    configuration: Configuration,
    private val getPomodoroStatsOverviewUseCase: GetPomodoroStatsOverviewUseCase,
    private val getPomodoroHistogramStatsUseCase: GetPomodoroHistogramStatsUseCase
): UseCase<GetPomodoroStatsUseCase.Request, GetPomodoroStatsUseCase.Response>(configuration) {

    override suspend fun process(request: Request): Flow<Response> =
        combine(
            getPomodoroStatsOverviewUseCase.process(
                GetPomodoroStatsOverviewUseCase.Request
            ),
            getPomodoroHistogramStatsUseCase.process(
                GetPomodoroHistogramStatsUseCase.Request(
                    calendarReportType = request.calendarReportType,
                    dateTime = request.dateTime
                )
            )
        ) { pomodoroStatsOverviewResponse, pomodoroHistogramStatsReponse ->
            Response(
                totalHours = pomodoroStatsOverviewResponse.totalHours,
                pomodoroCompleted = pomodoroStatsOverviewResponse.pomodoroCompleted,
                pomodoroStatsReport = pomodoroHistogramStatsReponse.pomodoroStatsReport
            )
        }

    data class Request(
        val calendarReportType: CalendarReportType,
        val dateTime: OffsetDateTime
    ): UseCase.Request

    data class Response(
        val totalHours: Int,
        val pomodoroCompleted: Int,
        val pomodoroStatsReport: PomodoroStatsReport
    ): UseCase.Response
}