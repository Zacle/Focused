package com.zn.apps.data_repository.repository

import com.zn.apps.datastore.FocusedUserPreferencesDataSource
import com.zn.apps.domain.repository.UserDataRepository
import com.zn.apps.model.UserData
import kotlinx.coroutines.flow.Flow

class DefaultUserDataRepository(
    private val focusedUserPreferencesDataSource: FocusedUserPreferencesDataSource
): UserDataRepository {

    override val userData: Flow<UserData> =
        focusedUserPreferencesDataSource.userData

    override suspend fun setShouldHideOnboarding(shouldHideOnboarding: Boolean) {
        focusedUserPreferencesDataSource.setShouldHideOnboarding(shouldHideOnboarding)
    }

    override suspend fun setPomodoroLength(pomodoroLength: Int) {
        focusedUserPreferencesDataSource.setPomodoroLength(pomodoroLength)
    }

    override suspend fun setShortBreakLength(shortBreakLength: Int) {
        focusedUserPreferencesDataSource.setShortBreakLength(shortBreakLength)
    }

    override suspend fun setLongBreakLength(longBreakLength: Int) {
        focusedUserPreferencesDataSource.setLongBreakLength(longBreakLength)
    }

    override suspend fun setNumberOfPomodoroBeforeLongBreak(numberOfPomodoroBeforeLongBreak: Int) {
        focusedUserPreferencesDataSource.setNumberOfPomodoroBeforeLongBreak(numberOfPomodoroBeforeLongBreak)
    }
}