package com.zn.apps.feature.timer.ui

import com.zn.apps.model.data.task.Task

data class TimerUiStateHolder(
    val showStopTimerDialog: Boolean = false,
    val showSkipBreakDialog: Boolean = false,
    val task: Task? = null
)
