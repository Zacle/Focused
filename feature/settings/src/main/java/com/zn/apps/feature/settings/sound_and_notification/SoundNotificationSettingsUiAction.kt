package com.zn.apps.feature.settings.sound_and_notification

import com.zn.apps.ui_common.state.UiAction

sealed class SoundNotificationSettingsUiAction: UiAction {
    data object Load: SoundNotificationSettingsUiAction()
    data class SetTaskReminderMinutes(val taskReminderMinutes: Int): SoundNotificationSettingsUiAction()
    data class SetDailyTodoReminder(val dailyTodoReminder: Boolean): SoundNotificationSettingsUiAction()
    data class SetSnoozeTaskReminder(val snoozeTaskReminder: Boolean): SoundNotificationSettingsUiAction()
    data class SetSnoozeTaskReminderAfter(val snoozeAfter: Int): SoundNotificationSettingsUiAction()
}