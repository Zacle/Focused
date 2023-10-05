package com.zn.apps.model

import com.zn.apps.model.data.pomodoro.PomodoroPhase

const val DEFAULT_SHORT_BREAK_LENGTH = 5
const val DEFAULT_LONG_BREAK_LENGTH = 15
const val DEFAULT_NUMBER_OF_POMODORO_BEFORE_LONG_BREAK = 4

data class UserData(
    val shouldHideOnboarding: Boolean,
    val pomodoroLength: Int,
    val shortBreakLength: Int,
    val longBreakLength: Int,
    val numberOfPomodoroBeforeLongBreak: Int
) {
    fun lengthInMinutesForPhase(phase: PomodoroPhase): Int =
        when(phase) {
            PomodoroPhase.POMODORO -> pomodoroLength
            PomodoroPhase.LONG_BREAK -> longBreakLength
            PomodoroPhase.SHORT_BREAK -> shortBreakLength
        }
}
