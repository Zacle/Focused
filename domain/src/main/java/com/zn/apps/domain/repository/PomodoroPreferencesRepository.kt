package com.zn.apps.domain.repository

import com.zn.apps.model.datastore.PomodoroPreferences
import kotlinx.coroutines.flow.Flow

interface PomodoroPreferencesRepository {

    /**
     * Stream of [PomodoroPreferences]
     */
    val pomodoroPreferences: Flow<PomodoroPreferences>

    /**
     * Set the default pomodoro length if no task's pomodoro is set
     */
    suspend fun setPomodoroLength(pomodoroLength: Int)

    /**
     * Set the default length of short break
     */
    suspend fun setShortBreakLength(shortBreakLength: Int)

    /**
     * Set the default length of long break
     */
    suspend fun setLongBreakLength(longBreakLength: Int)

    /**
     * Set the default number of pomodoro to be completed before the long break
     */
    suspend fun setNumberOfPomodoroBeforeLongBreak(numberOfPomodoroBeforeLongBreak: Int)

    /**
     * Set if the break should not be run after the pomodoro is completed
     */
    suspend fun setDisableBreak(disableBreak: Boolean)

    /**
     * Set if we should start the pomodoro immediately after the completion after the previous phase
     */
    suspend fun setAutoStartNextPomodoro(autoStartNextPomodoro: Boolean)

    /**
     * Set if we should start the break immediately after the pomodoro is completed
     */
    suspend fun setAutoStartBreak(autoStartBreak: Boolean)
}