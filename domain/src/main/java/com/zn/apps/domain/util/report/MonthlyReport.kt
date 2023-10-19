package com.zn.apps.domain.util.report

import com.zn.apps.common.millisecondsToHours
import com.zn.apps.model.data.report.ReportInterval
import com.zn.apps.model.data.report.ReportResource
import com.zn.apps.model.data.report.StatsReport
import com.zn.apps.model.data.task.TaskResource
import java.time.LocalDate
import java.time.OffsetDateTime
import java.time.temporal.TemporalAdjusters

class MonthlyReport(override val currentDay: OffsetDateTime): CalendarReport(currentDay) {

    override fun getReportInterval(): ReportInterval {
        val startTime = currentDay
            .with(TemporalAdjusters.firstDayOfMonth())
            .withHour(0)
            .withMinute(0)
            .withSecond(1)
        val endTime = currentDay
            .with(TemporalAdjusters.lastDayOfMonth())
            .withHour(23)
            .withMinute(59)
            .withSecond(59)
        return ReportInterval(startTime, endTime)
    }

    override fun getPomodoroStats(reportResources: List<ReportResource>): StatsReport {
        var totalHours: Long = 0
        var focusDays = 0
        val completedPomodoro = reportResources.size
        val monthLength = LocalDate
            .of(currentDay.year, currentDay.month, currentDay.dayOfMonth)
            .lengthOfMonth()
        val stats: MutableMap<Int, Int> = initStatMap(1, monthLength)
        reportResources.forEach { reportResource ->
            totalHours += reportResource.elapsedTime
            val dayOfMonthCompleted = reportResource.completedTime.dayOfMonth
            stats[dayOfMonthCompleted] = stats.getOrDefault(dayOfMonthCompleted, 0) + 1
        }
        stats.forEach { (_, pomodoroCompleted) ->
            focusDays += if (pomodoroCompleted > 0) 1 else 0
        }
        return StatsReport(
            focusHours = totalHours.millisecondsToHours(),
            focusDays = focusDays,
            totalCompleted = completedPomodoro,
            stats = stats.toMap()
        )
    }

    override fun getTaskStats(taskResources: List<TaskResource>): StatsReport {
        var totalHours: Long = 0
        var focusDays = 0
        val completedTasks = taskResources.size
        val monthLength = LocalDate
            .of(currentDay.year, currentDay.month, currentDay.dayOfMonth)
            .lengthOfMonth()
        val stats: MutableMap<Int, Int> = initStatMap(1, monthLength)
        taskResources.forEach { taskResource ->
            totalHours += taskResource.task.pomodoro.elapsedTime
            val dayOfMonthCompleted = taskResource.task.completedTime!!.dayOfMonth
            stats[dayOfMonthCompleted] = stats.getOrDefault(dayOfMonthCompleted, 0) + 1
        }
        stats.forEach { (_, taskCompleted) ->
            focusDays += if (taskCompleted > 0) 1 else 0
        }
        return StatsReport(
            focusHours = totalHours.millisecondsToHours(),
            focusDays = focusDays,
            totalCompleted = completedTasks,
            stats = stats.toMap()
        )
    }
}