package com.zn.apps.domain.report

import com.zn.apps.domain.UseCase
import com.zn.apps.domain.repository.ReportRepository
import com.zn.apps.domain.util.formatToString
import com.zn.apps.domain.util.report.CalendarReport
import com.zn.apps.model.data.report.CalendarReportType
import com.zn.apps.model.data.report.PomodoroStatsReport
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.OffsetDateTime

class GetPomodoroHistogramStatsUseCase(
    configuration: Configuration,
    private val reportRepository: ReportRepository
): UseCase<GetPomodoroHistogramStatsUseCase.Request, GetPomodoroHistogramStatsUseCase.Response>(configuration) {

    override suspend fun process(request: Request): Flow<Response> {
        val (calendarReportType, dateTime) = request
        val calendarReport = CalendarReport.getCalendarReport(
            calendarReportType = calendarReportType,
            currentDay = dateTime
        )
        val reportInterval = calendarReport.getReportInterval()
        return reportRepository.getReportResources(
            from = reportInterval.startTime.formatToString(),
            to = reportInterval.endTime.formatToString()
        ).map { reportResources ->
            Response(calendarReport.getPomodoroStats(reportResources))
        }
    }

    data class Request(
        val calendarReportType: CalendarReportType,
        val dateTime: OffsetDateTime
    ): UseCase.Request

    data class Response(val pomodoroStatsReport: PomodoroStatsReport): UseCase.Response
}