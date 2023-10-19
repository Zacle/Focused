package com.zn.apps.model.data.report

data class StatsReport(
    val focusHours: Int,
    val focusDays: Int,
    val totalCompleted: Int,
    val stats: Map<Int, Int>
)
