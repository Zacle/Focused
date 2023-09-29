package com.zn.apps.datastore

import androidx.datastore.core.DataStore
import com.zn.apps.model.DEFAULT_LONG_BREAK_LENGTH
import com.zn.apps.model.DEFAULT_NUMBER_OF_POMODORO_BEFORE_LONG_BREAK
import com.zn.apps.model.DEFAULT_SHORT_BREAK_LENGTH
import com.zn.apps.model.UserData
import com.zn.apps.model.data.pomodoro.POMODORO_LENGTH_IN_MINUTES_DEFAULT
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class FocusedUserPreferencesDataSource @Inject constructor(
    private val userPreferences: DataStore<UserPreferences>
) {
    val userData = userPreferences.data
        .map { userPreferences ->
            UserData(
                shouldHideOnboarding = userPreferences.shouldHideOnboarding,
                pomodoroLength =
                    if (userPreferences.pomodoroLength == 0)
                        POMODORO_LENGTH_IN_MINUTES_DEFAULT
                    else
                        userPreferences.pomodoroLength,
                shortBreakLength =
                    if (userPreferences.shortBreakLength == 0)
                        DEFAULT_SHORT_BREAK_LENGTH
                    else
                        userPreferences.shortBreakLength,
                longBreakLength =
                    if (userPreferences.longBreakLength == 0)
                        DEFAULT_LONG_BREAK_LENGTH
                    else
                        userPreferences.longBreakLength,
                numberOfPomodoroBeforeLongBreak =
                    if (userPreferences.numberOfPomodoroBeforeLongBreak == 0)
                        DEFAULT_NUMBER_OF_POMODORO_BEFORE_LONG_BREAK
                    else
                        userPreferences.numberOfPomodoroBeforeLongBreak
            )
        }

    suspend fun setShouldHideOnboarding(shouldHideOnboarding: Boolean) {
        userPreferences.updateData {
            it.copy {
                this.shouldHideOnboarding = shouldHideOnboarding
            }
        }
    }

    suspend fun setPomodoroLength(pomodoroLength: Int) {
        userPreferences.updateData {
            it.copy {
                this.pomodoroLength = pomodoroLength
            }
        }
    }

    suspend fun setShortBreakLength(shortBreakLength: Int) {
        userPreferences.updateData {
            it.copy {
                this.shortBreakLength = shortBreakLength
            }
        }
    }

    suspend fun setLongBreakLength(longBreakLength: Int) {
        userPreferences.updateData {
            it.copy {
                this.longBreakLength = longBreakLength
            }
        }
    }

    suspend fun setNumberOfPomodoroBeforeLongBreak(numberOfPomodoroBeforeLongBreak: Int) {
        userPreferences.updateData {
            it.copy {
                this.numberOfPomodoroBeforeLongBreak = numberOfPomodoroBeforeLongBreak
            }
        }
    }
}