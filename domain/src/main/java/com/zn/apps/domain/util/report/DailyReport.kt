package com.zn.apps.domain.util.report

import com.zn.apps.common.millisecondsToHours
import com.zn.apps.model.data.report.ReportInterval
import com.zn.apps.model.data.report.ReportResource
import com.zn.apps.model.data.report.StatsReport
import com.zn.apps.model.data.task.TaskResource
import java.time.OffsetDateTime

class DailyReport(override val currentDay: OffsetDateTime): CalendarReport(currentDay) {

    override fun getReportInterval(): ReportInterval {
        val startTime = currentDay
            .withHour(0)
            .withMinute(0)
            .withSecond(1)
        val endTime = currentDay
            .withHour(23)
            .withMinute(59)
            .withSecond(59)
        return ReportInterval(startTime, endTime)
    }

    override fun getPomodoroStats(reportResources: List<ReportResource>): StatsReport {
        var totalHours: Long = 0
        val completedPomodoro = reportResources.size
        val stats: MutableMap<Int, Int> = initStatMap(0, 23)
        reportResources.forEach { reportResource ->
            totalHours += reportResource.elapsedTime
            val hourCompleted = reportResource.completedTime.hour
            stats[hourCompleted] = stats.getOrDefault(hourCompleted, 0) + 1
        }
        return StatsReport(
            focusHours = totalHours.millisecondsToHours(),
            focusDays = 0,
            totalCompleted = completedPomodoro,
            stats = stats.toMap()
        )
    }

    override fun getTaskStats(taskResources: List<TaskResource>): StatsReport {
        var totalHours: Long = 0
        val completedTasks = taskResources.size
        val stats: MutableMap<Int, Int> = initStatMap(0, 23)
        taskResources.forEach { taskResource ->
            totalHours += taskResource.task.pomodoro.elapsedTime
            val hourCompleted = taskResource.task.completedTime!!.hour
            stats[hourCompleted] = stats.getOrDefault(hourCompleted, 0) + 1
        }
        return StatsReport(
            focusHours = totalHours.millisecondsToHours(),
            focusDays = 0,
            totalCompleted = completedTasks,
            stats = stats
        )
    }
}