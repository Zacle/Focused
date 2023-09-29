package com.zn.apps.domain.repository

import com.zn.apps.model.UserData
import kotlinx.coroutines.flow.Flow

interface UserDataRepository {

    /**
     * Stream of [UserData]
     */
    val userData: Flow<UserData>

    /**
     * Sets whether the user has completed the onboarding process
     */
    suspend fun setShouldHideOnboarding(shouldHideOnboarding: Boolean)

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
}