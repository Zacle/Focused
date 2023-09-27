package com.zn.apps.model

const val DEFAULT_SHORT_BREAK_LENGTH = 5
const val DEFAULT_LONG_BREAK_LENGTH = 15
const val DEFAULT_NUMBER_OF_POMODORO_BEFORE_LONG_BREAK = 4

data class UserData(
    val shouldHideOnboarding: Boolean,
    val pomodoroLength: Int,
    val shortBreakLength: Int,
    val longBreakLength: Int,
    val numberOfPomodoroBeforeLongBreak: Int
)
