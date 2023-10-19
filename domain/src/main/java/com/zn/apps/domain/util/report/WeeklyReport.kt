package com.zn.apps.domain.util.report

import com.zn.apps.common.millisecondsToHours
import com.zn.apps.model.data.report.ReportInterval
import com.zn.apps.model.data.report.ReportResource
import com.zn.apps.model.data.report.StatsReport
import com.zn.apps.model.data.task.TaskResource
import java.time.DayOfWeek
import java.time.OffsetDateTime
import java.time.temporal.TemporalAdjusters
import java.time.temporal.WeekFields
import java.util.Locale

class WeeklyReport(override val currentDay: OffsetDateTime): CalendarReport(currentDay) {

    override fun getReportInterval(): ReportInterval {
        val firstDayOfWeek: DayOfWeek = WeekFields.of(Locale.getDefault()).firstDayOfWeek
        val startTime = currentDay
            .with(TemporalAdjusters.previousOrSame(firstDayOfWeek))
            .withHour(0)
            .withMinute(0)
            .withSecond(1)
        val endDayOfWeek: DayOfWeek = firstDayOfWeek.plus(6)
        val endTime = currentDay
            .with(TemporalAdjusters.previousOrSame(endDayOfWeek))
            .withHour(23)
            .withMinute(59)
            .withSecond(59)
        return ReportInterval(startTime, endTime)
    }

    override fun getPomodoroStats(reportResources: List<ReportResource>): StatsReport {
        var totalHours: Long = 0
        var focusDays = 0
        val completedPomodoro = reportResources.size
        val stats: MutableMap<Int, Int> = initStatMap(1, 7)
        reportResources.forEach { reportResource ->
            totalHours += reportResource.elapsedTime
            val dayOfWeekCompleted = reportResource.completedTime.dayOfWeek.value
            stats[dayOfWeekCompleted] = stats.getOrDefault(dayOfWeekCompleted, 0) + 1
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
        val stats: MutableMap<Int, Int> = initStatMap(1, 7)
        taskResources.forEach { taskResource ->
            totalHours += taskResource.task.pomodoro.elapsedTime
            val dayOfWeekCompleted = taskResource.task.completedTime!!.dayOfWeek.value
            stats[dayOfWeekCompleted] = stats.getOrDefault(dayOfWeekCompleted, 0) + 1
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