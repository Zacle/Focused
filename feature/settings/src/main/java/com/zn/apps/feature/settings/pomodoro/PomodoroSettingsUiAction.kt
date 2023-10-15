package com.zn.apps.feature.settings.pomodoro

import com.zn.apps.ui_common.state.UiAction

sealed class PomodoroSettingsUiAction: UiAction {
    data object Load: PomodoroSettingsUiAction()
    data class SetPomodoroLength(val pomodoroLength: Int): PomodoroSettingsUiAction()
    data class SetLongBreakLength(val longBreakLength: Int): PomodoroSettingsUiAction()
    data class SetShortBreakLength(val shortBreakLength: Int): PomodoroSettingsUiAction()
    data class SetLongBreakAfter(val longBreakAfter: Int): PomodoroSettingsUiAction()
    data class SetDisableBreak(val disableBreak: Boolean): PomodoroSettingsUiAction()
    data class SetAutoStartNextPomodoro(val autoStartNextPomodoro: Boolean): PomodoroSettingsUiAction()
    data class SetAutoStartBreak(val autoStartBreak: Boolean): PomodoroSettingsUiAction()
}
