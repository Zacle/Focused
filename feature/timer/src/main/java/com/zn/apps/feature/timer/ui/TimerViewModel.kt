package com.zn.apps.feature.timer.ui

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.zn.apps.core.timer.TimerManager
import com.zn.apps.domain.task.GetTaskUseCase
import com.zn.apps.domain.task.UpsertTaskUseCase
import com.zn.apps.feature.timer.ui.TimerUiAction.Load
import com.zn.apps.feature.timer.ui.TimerUiAction.PauseTimer
import com.zn.apps.feature.timer.ui.TimerUiAction.SkipBreakConfirmed
import com.zn.apps.feature.timer.ui.TimerUiAction.SkipBreakDismissed
import com.zn.apps.feature.timer.ui.TimerUiAction.SkipBreakPressed
import com.zn.apps.feature.timer.ui.TimerUiAction.StartTimer
import com.zn.apps.feature.timer.ui.TimerUiAction.StopTimerConfirmed
import com.zn.apps.feature.timer.ui.TimerUiAction.StopTimerDismissed
import com.zn.apps.feature.timer.ui.TimerUiAction.StopTimerPressed
import com.zn.apps.model.data.task.Task
import com.zn.apps.model.usecase.Result
import com.zn.apps.ui_common.state.BaseViewModel
import com.zn.apps.ui_common.state.UiEvent
import com.zn.apps.ui_common.state.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.OffsetDateTime
import javax.inject.Inject

const val TIMER_TASK_ID_ARGUMENT = "taskId"

@HiltViewModel
class TimerViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val upsertTaskUseCase: UpsertTaskUseCase,
    private val getTaskUseCase: GetTaskUseCase,
    private val timerManager: TimerManager
): BaseViewModel<Unit, UiState<Unit>, TimerUiAction, UiEvent>() {

    /** Get the task id that was started by pressing on the task's start button **/
    private val routeTaskId: String? = savedStateHandle[TIMER_TASK_ID_ARGUMENT]

    var uiStateHolder = MutableStateFlow(TimerUiStateHolder())
        private set

    val pomodoroState = timerManager.pomodoroTimerState

    override fun initState(): UiState<Unit> = UiState.Loading

    init {
        submitAction(Load)
    }

    override fun handleAction(action: TimerUiAction) {
        when(action) {
            Load -> loadTask()
            StartTimer -> startTimer()
            PauseTimer -> pauseTimer()
            StopTimerPressed -> stopTimerPressed()
            StopTimerDismissed -> stopTimerDismissed()
            StopTimerConfirmed -> stopTimerConformed()
            SkipBreakPressed -> skipBreakPressed()
            SkipBreakDismissed -> skipBreakDismissed()
            SkipBreakConfirmed -> skipBreakConfirmed()
            is TimerUiAction.CompleteTask -> setTaskCompleted(action.task)
        }
    }

    private fun setTaskCompleted(task: Task) {
        viewModelScope.launch {
            val taskToComplete = task.copy(completed = true, completedTime = OffsetDateTime.now())
            upsertTaskUseCase.execute(
                UpsertTaskUseCase.Request(taskToComplete)
            )
        }
    }

    private fun skipBreakConfirmed() {
        uiStateHolder.update {
            it.copy(showSkipBreakDialog = false)
        }
        timerManager.skipBreak()
    }

    private fun skipBreakDismissed() {
        uiStateHolder.update {
            it.copy(showSkipBreakDialog = false)
        }
    }

    private fun skipBreakPressed() {
        uiStateHolder.update {
            it.copy(showSkipBreakDialog = true)
        }
    }

    private fun stopTimerConformed() {
        uiStateHolder.update {
            it.copy(showStopTimerDialog = false)
        }
        timerManager.stopTimer()
    }

    private fun stopTimerDismissed() {
        uiStateHolder.update {
            it.copy(showStopTimerDialog = false)
        }
    }

    private fun stopTimerPressed() {
        uiStateHolder.update {
            it.copy(showStopTimerDialog = true)
        }
    }

    private fun pauseTimer() {
        timerManager.pauseTimer()
    }

    private fun startTimer() {
        timerManager.startTimer()
    }

    private fun loadTask() {
        viewModelScope.launch {
            /** task id that was saved in the datastore **/
            val taskId = pomodoroState.first().taskIdRunning

            /** verify if a task is already running or get the id from the task that was started **/
            val runningTaskId = when {
                routeTaskId != null -> routeTaskId
                taskId != "" -> taskId
                else -> ""
            }

            getTaskUseCase.execute(
                GetTaskUseCase.Request(runningTaskId)
            ).collectLatest { result ->
                if (result is Result.Success) {
                    val task: Task = result.data.taskResource.task
                    uiStateHolder.update {
                        it.copy(task = task)
                    }
                }
            }
        }
    }
}