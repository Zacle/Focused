package com.zn.apps.feature.settings.sound_and_notification

import androidx.lifecycle.viewModelScope
import com.zn.apps.common.alarm.DailyTodoReminderAlarmScheduler
import com.zn.apps.domain.datastore.GetReminderPreferencesUseCase
import com.zn.apps.domain.repository.ReminderPreferencesRepository
import com.zn.apps.feature.settings.sound_and_notification.SoundNotificationSettingsUiAction.Load
import com.zn.apps.feature.settings.sound_and_notification.SoundNotificationSettingsUiAction.SetDailyTodoReminder
import com.zn.apps.feature.settings.sound_and_notification.SoundNotificationSettingsUiAction.SetSnoozeTaskReminder
import com.zn.apps.feature.settings.sound_and_notification.SoundNotificationSettingsUiAction.SetSnoozeTaskReminderAfter
import com.zn.apps.feature.settings.sound_and_notification.SoundNotificationSettingsUiAction.SetTaskReminderMinutes
import com.zn.apps.ui_common.state.BaseViewModel
import com.zn.apps.ui_common.state.UiEvent
import com.zn.apps.ui_common.state.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SoundNotificationSettingsViewModel @Inject constructor(
    private val getReminderPreferencesUseCase: GetReminderPreferencesUseCase,
    private val reminderPreferencesRepository: ReminderPreferencesRepository,
    private val converter: SoundNotificationSettingsUiConverter,
    private val dailyTodoReminderAlarmScheduler: DailyTodoReminderAlarmScheduler
): BaseViewModel<SoundNotificationSettingsUiModel, UiState<SoundNotificationSettingsUiModel>, SoundNotificationSettingsUiAction, UiEvent>() {

    override fun initState(): UiState<SoundNotificationSettingsUiModel> = UiState.Loading

    init {
        submitAction(Load)
    }

    override fun handleAction(action: SoundNotificationSettingsUiAction) {
        when(action) {
            Load -> loadReminderPreferences()
            is SetTaskReminderMinutes -> setTaskReminderMinutes(action.taskReminderMinutes)
            is SetDailyTodoReminder -> setDailyTodoReminder(action.dailyTodoReminder)
            is SetSnoozeTaskReminder -> setSnoozeTaskReminder(action.snoozeTaskReminder)
            is SetSnoozeTaskReminderAfter -> setSnoozeTaskReminderAfter(action.snoozeAfter)
        }
    }

    private fun setSnoozeTaskReminderAfter(snoozeAfter: Int) {
        viewModelScope.launch {
            reminderPreferencesRepository.setSnoozeAfter(snoozeAfter)
        }
    }

    private fun setSnoozeTaskReminder(snoozeTaskReminder: Boolean) {
        viewModelScope.launch {
            reminderPreferencesRepository.setSnoozeTaskReminder(snoozeTaskReminder)
        }
    }

    private fun setDailyTodoReminder(dailyTodoReminder: Boolean) {
        viewModelScope.launch {
            if (dailyTodoReminder) {
                dailyTodoReminderAlarmScheduler.scheduleAlarm()
            } else {
                dailyTodoReminderAlarmScheduler.cancel()
            }
            reminderPreferencesRepository.setTodoReminder(dailyTodoReminder)
        }
    }

    private fun setTaskReminderMinutes(taskReminderMinutes: Int) {
        viewModelScope.launch {
            reminderPreferencesRepository.setTaskReminder(taskReminderMinutes)
        }
    }

    private fun loadReminderPreferences() {
        viewModelScope.launch {
            getReminderPreferencesUseCase.execute(
                GetReminderPreferencesUseCase.Request
            ).collectLatest { result ->
                submitState(converter.convert(result))
            }
        }
    }
}