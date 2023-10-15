package com.zn.apps.feature.settings.pomodoro

import androidx.lifecycle.viewModelScope
import com.zn.apps.domain.datastore.GetPomodoroPreferencesUseCase
import com.zn.apps.domain.repository.PomodoroPreferencesRepository
import com.zn.apps.feature.settings.pomodoro.PomodoroSettingsUiAction.Load
import com.zn.apps.feature.settings.pomodoro.PomodoroSettingsUiAction.SetAutoStartBreak
import com.zn.apps.feature.settings.pomodoro.PomodoroSettingsUiAction.SetAutoStartNextPomodoro
import com.zn.apps.feature.settings.pomodoro.PomodoroSettingsUiAction.SetDisableBreak
import com.zn.apps.feature.settings.pomodoro.PomodoroSettingsUiAction.SetLongBreakAfter
import com.zn.apps.feature.settings.pomodoro.PomodoroSettingsUiAction.SetLongBreakLength
import com.zn.apps.feature.settings.pomodoro.PomodoroSettingsUiAction.SetPomodoroLength
import com.zn.apps.feature.settings.pomodoro.PomodoroSettingsUiAction.SetShortBreakLength
import com.zn.apps.ui_common.state.BaseViewModel
import com.zn.apps.ui_common.state.UiEvent
import com.zn.apps.ui_common.state.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PomodoroSettingsViewModel @Inject constructor(
    private val getPomodoroPreferencesUseCase: GetPomodoroPreferencesUseCase,
    private val pomodoroPreferencesRepository: PomodoroPreferencesRepository,
    private val converter: PomodoroSettingsUiConverter
): BaseViewModel<PomodoroSettingsUiModel, UiState<PomodoroSettingsUiModel>, PomodoroSettingsUiAction, UiEvent>() {

    override fun initState(): UiState<PomodoroSettingsUiModel> = UiState.Loading

    init {
        submitAction(Load)
    }

    override fun handleAction(action: PomodoroSettingsUiAction) {
        when(action) {
            Load -> loadPomodoroPreferences()
            is SetPomodoroLength -> setPomodoroLength(action.pomodoroLength)
            is SetLongBreakLength -> setLongBreakLength(action.longBreakLength)
            is SetShortBreakLength -> setShortBreakLength(action.shortBreakLength)
            is SetLongBreakAfter -> setLongBreakAfter(action.longBreakAfter)
            is SetDisableBreak -> setDisableBreak(action.disableBreak)
            is SetAutoStartNextPomodoro -> setAutoStartNextPomodoro(action.autoStartNextPomodoro)
            is SetAutoStartBreak -> setAutoStartBreak(action.autoStartBreak)
        }
    }

    private fun setAutoStartBreak(autoStartBreak: Boolean) {
        viewModelScope.launch {
            pomodoroPreferencesRepository.setAutoStartBreak(autoStartBreak)
        }
    }

    private fun setAutoStartNextPomodoro(autoStartNextPomodoro: Boolean) {
        viewModelScope.launch {
            pomodoroPreferencesRepository.setAutoStartNextPomodoro(autoStartNextPomodoro)
        }
    }

    private fun setDisableBreak(disableBreak: Boolean) {
        viewModelScope.launch {
            pomodoroPreferencesRepository.setDisableBreak(disableBreak)
        }
    }

    private fun setLongBreakAfter(longBreakAfter: Int) {
        viewModelScope.launch {
            pomodoroPreferencesRepository.setNumberOfPomodoroBeforeLongBreak(longBreakAfter)
        }
    }

    private fun setShortBreakLength(shortBreakLength: Int) {
        viewModelScope.launch {
            pomodoroPreferencesRepository.setShortBreakLength(shortBreakLength)
        }
    }

    private fun setLongBreakLength(longBreakLength: Int) {
        viewModelScope.launch {
            pomodoroPreferencesRepository.setLongBreakLength(longBreakLength)
        }
    }

    private fun setPomodoroLength(pomodoroLength: Int) {
        viewModelScope.launch {
            pomodoroPreferencesRepository.setPomodoroLength(pomodoroLength)
        }
    }

    private fun loadPomodoroPreferences() {
        viewModelScope.launch {
            getPomodoroPreferencesUseCase.execute(
                GetPomodoroPreferencesUseCase.Request
            ).collectLatest { result ->
                submitState(converter.convert(result))
            }
        }
    }
}