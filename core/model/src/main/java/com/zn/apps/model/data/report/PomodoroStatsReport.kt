package com.zn.apps.model.data.report

data class PomodoroStatsReport(
    val focusHours: Int,
    val focusDays: Int,
    val completedPomodoro: Int,
    val stats: Map<Int, Int>
)
