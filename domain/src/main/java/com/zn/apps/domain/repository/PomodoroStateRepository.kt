package com.zn.apps.domain.repository

import com.zn.apps.model.data.pomodoro.PomodoroPhase
import com.zn.apps.model.data.pomodoro.PomodoroState
import com.zn.apps.model.data.pomodoro.TimerState
import kotlinx.coroutines.flow.Flow

interface PomodoroStateRepository {

    /**
     * Stream of [PomodoroState]
     */
    val pomodoroState: Flow<PomodoroState>

    /**
     * Set the current timer's state: [TimerState.IDLE], [TimerState.PAUSED] or [TimerState.RUNNING]
     */
    suspend fun setTimerState(timerState: TimerState)

    /**
     * Set the current phase: [PomodoroPhase.POMODORO], [PomodoroPhase.SHORT_BREAK]
     * or [PomodoroPhase.LONG_BREAK]
     */
    suspend fun setCurrentPhase(currentPhase: PomodoroPhase)

    /**
     * Set the remaining time to finish the timer
     */
    suspend fun setTimeLeftInMillis(timeLeftInMillis: Long)

    /**
     * Set the timer time to run
     */
    suspend fun setTargetTimeInMillis(targetTimeInMillis: Long)

    /**
     * Set the id of the running task if any
     */
    suspend fun setTaskIdRunning(taskIdRunning: String)

    /**
     * Set the number of pomodoro completed so far since running the task
     */
    suspend fun setPomodoroCompletedSoFar(pomodoroCompletedSoFar: Int)

    /**
     * Set the number of pomodoro to be completed before long break
     */
    suspend fun setPomodoroToBeCompletedBeforeLongBreak(pomodoroToBeCompletedBeforeLongBreak: Int)
}