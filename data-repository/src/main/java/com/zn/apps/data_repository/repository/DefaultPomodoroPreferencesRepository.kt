package com.zn.apps.data_repository.repository

import com.zn.apps.datastore.PomodoroPreferencesDataSource
import com.zn.apps.domain.repository.PomodoroPreferencesRepository
import com.zn.apps.model.datastore.PomodoroPreferences
import kotlinx.coroutines.flow.Flow

class DefaultPomodoroPreferencesRepository(
    private val pomodoroPreferencesDataSource: PomodoroPreferencesDataSource
): PomodoroPreferencesRepository {

    override val pomodoroPreferences: Flow<PomodoroPreferences>
        get() = pomodoroPreferencesDataSource.pomodoroPreferences

    override suspend fun setPomodoroLength(pomodoroLength: Int) {
        pomodoroPreferencesDataSource.setPomodoroLength(pomodoroLength)
    }

    override suspend fun setShortBreakLength(shortBreakLength: Int) {
        pomodoroPreferencesDataSource.setShortBreakLength(shortBreakLength)
    }

    override suspend fun setLongBreakLength(longBreakLength: Int) {
        pomodoroPreferencesDataSource.setLongBreakLength(longBreakLength)
    }

    override suspend fun setNumberOfPomodoroBeforeLongBreak(numberOfPomodoroBeforeLongBreak: Int) {
        pomodoroPreferencesDataSource.setNumberOfPomodoroBeforeLongBreak(numberOfPomodoroBeforeLongBreak)
    }

    override suspend fun setDisableBreak(disableBreak: Boolean) {
        pomodoroPreferencesDataSource.setDisableBreak(disableBreak)
    }

    override suspend fun setAutoStartNextPomodoro(autoStartNextPomodoro: Boolean) {
        pomodoroPreferencesDataSource.setAutoStartNextPomodoro(autoStartNextPomodoro)
    }

    override suspend fun setAutoStartBreak(autoStartBreak: Boolean) {
        pomodoroPreferencesDataSource.setAutoStartBreak(autoStartBreak)
    }
}