package com.zn.apps.feature.timer.ui

import com.zn.apps.model.data.task.Task
import com.zn.apps.ui_common.state.UiAction

sealed class TimerUiAction: UiAction {
    data object Load: TimerUiAction()
    data object StartTimer: TimerUiAction()
    data object PauseTimer: TimerUiAction()
    data class CompleteTask(val task: Task): TimerUiAction()
    data object StopTimerPressed: TimerUiAction()
    data object StopTimerDismissed: TimerUiAction()
    data object StopTimerConfirmed: TimerUiAction()
    data object SkipBreakPressed: TimerUiAction()
    data object SkipBreakDismissed: TimerUiAction()
    data object SkipBreakConfirmed: TimerUiAction()
}
