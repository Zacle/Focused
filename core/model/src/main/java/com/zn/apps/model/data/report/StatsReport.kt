package com.zn.apps.model.data.report

data class StatsReport(
    val focusHours: Int = 0,
    val focusDays: Int = 0,
    val totalCompleted: Int = 0,
    val stats: Map<Int, Int> = emptyMap()
)
