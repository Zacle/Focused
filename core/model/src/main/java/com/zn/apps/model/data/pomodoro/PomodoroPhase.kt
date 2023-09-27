package com.zn.apps.model.data.pomodoro

import com.zn.apps.model.R

enum class PomodoroPhase(val phaseName: Int) {
    POMODORO(R.string.pomodoro),
    LONG_BREAK(R.string.long_break),
    SHORT_BREAK(R.string.short_break);

    val isBreak get() = this == SHORT_BREAK || this == LONG_BREAK
}