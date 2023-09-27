package com.zn.apps.model.data.pomodoro

import com.zn.apps.common.minutesToMilliseconds

const val POMODORO_LENGTH_IN_MINUTES_DEFAULT = 25

data class PomodoroState(
    val timerState: TimerState,
    val currentPhase: PomodoroPhase,
    val timeLeftInMillis: Long,
    val targetTimeInMillis: Long,
    val taskIdRunning: String,
    val pomodoroCompletedSoFar: Int,
    val pomodoroToBeCompletedBeforeLongBreak: Int
) {
    companion object {
        val initialState = PomodoroState(
            timerState = TimerState.IDLE,
            currentPhase = PomodoroPhase.POMODORO,
            timeLeftInMillis = POMODORO_LENGTH_IN_MINUTES_DEFAULT.minutesToMilliseconds(),
            targetTimeInMillis = POMODORO_LENGTH_IN_MINUTES_DEFAULT.minutesToMilliseconds(),
            taskIdRunning = "",
            pomodoroCompletedSoFar = 0,
            pomodoroToBeCompletedBeforeLongBreak = 0
        )
    }
}

enum class TimerState {
    IDLE,
    PAUSED,
    RUNNING
}
