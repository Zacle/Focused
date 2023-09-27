package com.zn.apps.datastore

import androidx.datastore.core.DataStore
import com.zn.apps.datastore.PomodoroStateManager.PomodoroPhase
import com.zn.apps.datastore.PomodoroStateManager.TimerState
import com.zn.apps.model.data.pomodoro.PomodoroState
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import com.zn.apps.model.data.pomodoro.PomodoroPhase as PomodoroStatePhase
import com.zn.apps.model.data.pomodoro.TimerState as PomodoroTimerState

class PomodoroStateDataSource @Inject constructor(
    private val pomodoroStateManager: DataStore<PomodoroStateManager>
) {
    val pomodoroState = pomodoroStateManager.data
        .map { stateManager ->
            PomodoroState(
                timerState = toPomodoroTimerState(stateManager.timerState),
                currentPhase = toPomodoroStatePhase(stateManager.currentPhase),
                timeLeftInMillis = stateManager.timeLeftInMillis,
                targetTimeInMillis = stateManager.targetTimeInMillis,
                taskIdRunning = stateManager.taskIdRunning,
                pomodoroCompletedSoFar = stateManager.pomodoroCompletedSoFar,
                pomodoroToBeCompletedBeforeLongBreak = stateManager.pomodoroToBeCompletedBeforeLongBreak
            )
        }

    suspend fun setTimerState(timerState: PomodoroTimerState) {
        pomodoroStateManager.updateData {
            it.copy {
                this.timerState = toDataStoreTimerState(timerState)
            }
        }
    }

    suspend fun setCurrentPhase(currentPhase: PomodoroStatePhase) {
        pomodoroStateManager.updateData {
            it.copy {
                this.currentPhase = toDataStorePomodoroPhase(currentPhase)
            }
        }
    }

    suspend fun setTimeLeftInMillis(timeLeftInMillis: Long) {
        pomodoroStateManager.updateData {
            it.copy {
                this.timeLeftInMillis = timeLeftInMillis
            }
        }
    }

    suspend fun setTaskIdRunning(taskIdRunning: String) {
        pomodoroStateManager.updateData {
            it.copy {
                this.taskIdRunning = taskIdRunning
            }
        }
    }

    suspend fun setPomodoroCompletedSoFar(pomodoroCompletedSoFar: Int) {
        pomodoroStateManager.updateData {
            it.copy {
                this.pomodoroCompletedSoFar = pomodoroCompletedSoFar
            }
        }
    }

    suspend fun setTargetTimeInMillis(targetTimeInMillis: Long) {
        pomodoroStateManager.updateData {
            it.copy {
                this.targetTimeInMillis = targetTimeInMillis
            }
        }
    }

    suspend fun setPomodoroToBeCompletedBeforeLongBreak(pomodoroToBeCompletedBeforeLongBreak: Int) {
        pomodoroStateManager.updateData {
            it.copy {
                this.pomodoroToBeCompletedBeforeLongBreak = pomodoroToBeCompletedBeforeLongBreak
            }
        }
    }

    private fun toPomodoroTimerState(timerState: TimerState): PomodoroTimerState =
        when(timerState) {
            TimerState.PAUSED -> PomodoroTimerState.PAUSED
            TimerState.RUNNING -> PomodoroTimerState.RUNNING
            else -> PomodoroTimerState.IDLE
        }

    private fun toPomodoroStatePhase(pomodoroPhase: PomodoroPhase): PomodoroStatePhase =
        when(pomodoroPhase) {
            PomodoroPhase.SHORT_BREAK -> PomodoroStatePhase.SHORT_BREAK
            PomodoroPhase.LONG_BREAK -> PomodoroStatePhase.LONG_BREAK
            else -> PomodoroStatePhase.POMODORO
        }

    private fun toDataStoreTimerState(timerState: PomodoroTimerState): TimerState =
        when(timerState) {
            PomodoroTimerState.RUNNING -> TimerState.RUNNING
            PomodoroTimerState.PAUSED -> TimerState.PAUSED
            PomodoroTimerState.IDLE -> TimerState.IDLE
        }

    private fun toDataStorePomodoroPhase(pomodoroPhase: PomodoroStatePhase): PomodoroPhase =
        when(pomodoroPhase) {
            PomodoroStatePhase.POMODORO -> PomodoroPhase.POMODORO
            PomodoroStatePhase.LONG_BREAK -> PomodoroPhase.LONG_BREAK
            PomodoroStatePhase.SHORT_BREAK -> PomodoroPhase.SHORT_BREAK
        }
}