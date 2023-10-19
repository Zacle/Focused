package com.zn.apps.domain.util.report

import com.zn.apps.model.data.report.CalendarReportType
import com.zn.apps.model.data.report.ReportInterval
import com.zn.apps.model.data.report.ReportResource
import com.zn.apps.model.data.report.StatsReport
import com.zn.apps.model.data.task.TaskResource
import java.time.OffsetDateTime

abstract class CalendarReport(open val currentDay: OffsetDateTime) {

    abstract fun getReportInterval(): ReportInterval

    abstract fun getPomodoroStats(reportResources: List<ReportResource>): StatsReport

    abstract fun getTaskStats(taskResources: List<TaskResource>): StatsReport

    fun initStatMap(start: Int, end: Int): MutableMap<Int, Int> {
        val statsMap = mutableMapOf<Int, Int>()
        (start..end).forEach {
            statsMap[it] = 0
        }
        return statsMap
    }

    companion object {
        fun getCalendarReport(
            calendarReportType: CalendarReportType,
            currentDay: OffsetDateTime
        ): CalendarReport =
            when(calendarReportType) {
                CalendarReportType.DAILY -> DailyReport(currentDay)
                CalendarReportType.WEEKLY -> WeeklyReport(currentDay)
                CalendarReportType.MONTHLY -> MonthlyReport(currentDay)
            }
    }
}