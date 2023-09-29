package com.zn.apps.data_repository.repository

import com.zn.apps.datastore.PomodoroStateDataSource
import com.zn.apps.domain.repository.PomodoroStateRepository
import com.zn.apps.model.data.pomodoro.PomodoroPhase
import com.zn.apps.model.data.pomodoro.PomodoroState
import com.zn.apps.model.data.pomodoro.TimerState
import kotlinx.coroutines.flow.Flow

class DefaultPomodoroStateRepository(
    private val pomodoroStateDataSource: PomodoroStateDataSource
): PomodoroStateRepository {

    override val pomodoroState: Flow<PomodoroState>
        get() = pomodoroStateDataSource.pomodoroState

    override suspend fun setTimerState(timerState: TimerState) {
        pomodoroStateDataSource.setTimerState(timerState)
    }

    override suspend fun setCurrentPhase(currentPhase: PomodoroPhase) {
        pomodoroStateDataSource.setCurrentPhase(currentPhase)
    }

    override suspend fun setTimeLeftInMillis(timeLeftInMillis: Long) {
        pomodoroStateDataSource.setTimeLeftInMillis(timeLeftInMillis)
    }

    override suspend fun setTargetTimeInMillis(targetTimeInMillis: Long) {
        pomodoroStateDataSource.setTargetTimeInMillis(targetTimeInMillis)
    }

    override suspend fun setTaskIdRunning(taskIdRunning: String) {
        pomodoroStateDataSource.setTaskIdRunning(taskIdRunning)
    }

    override suspend fun setPomodoroCompletedSoFar(pomodoroCompletedSoFar: Int) {
        pomodoroStateDataSource.setPomodoroCompletedSoFar(pomodoroCompletedSoFar)
    }

    override suspend fun setPomodoroToBeCompletedBeforeLongBreak(
        pomodoroToBeCompletedBeforeLongBreak: Int
    ) {
        pomodoroStateDataSource.setPomodoroToBeCompletedBeforeLongBreak(pomodoroToBeCompletedBeforeLongBreak)
    }
}