package com.zn.apps.feature.tasks.list

import androidx.lifecycle.viewModelScope
import com.zn.apps.core.timer.TimerManager
import com.zn.apps.domain.GetTasksWithTagsUseCase
import com.zn.apps.feature.tasks.list.TasksUiAction.DeleteTaskConfirmed
import com.zn.apps.feature.tasks.list.TasksUiAction.DeleteTaskDismissed
import com.zn.apps.feature.tasks.list.TasksUiAction.DeleteTaskPressed
import com.zn.apps.feature.tasks.list.TasksUiAction.Load
import com.zn.apps.feature.tasks.list.TasksUiAction.NavigateToRelatedTasks
import com.zn.apps.feature.tasks.list.TasksUiAction.NavigateToTask
import com.zn.apps.feature.tasks.list.TasksUiAction.StartRunningTaskPressed
import com.zn.apps.feature.tasks.list.TasksUiAction.TagPressed
import com.zn.apps.feature.tasks.list.TasksUiAction.TaskCompleted
import com.zn.apps.feature.tasks.list.TasksUiAction.UpdateDueDateDismissed
import com.zn.apps.feature.tasks.list.TasksUiAction.UpdateDueDatePressed
import com.zn.apps.feature.tasks.list.TasksUiAction.UpdatePomodoroConfirmed
import com.zn.apps.feature.tasks.list.TasksUiAction.UpdatePomodoroDismissed
import com.zn.apps.feature.tasks.list.TasksUiAction.UpdatePomodoroPressed
import com.zn.apps.feature.tasks.list.TasksUiAction.UpdatedDueDateConfirmed
import com.zn.apps.filter.Filter
import com.zn.apps.filter.Grouping
import com.zn.apps.model.data.pomodoro.TimerState
import com.zn.apps.model.data.task.Task
import com.zn.apps.ui_common.delegate.TasksViewModelDelegate
import com.zn.apps.ui_common.state.BaseViewModel
import com.zn.apps.ui_common.state.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TasksViewModel @Inject constructor(
    private val getTasksWithTagsUseCase: GetTasksWithTagsUseCase,
    private val tasksViewModelDelegate: TasksViewModelDelegate,
    private val converter: TasksUiConverter,
    private val timerManager: TimerManager
): BaseViewModel<TasksUiModel, UiState<TasksUiModel>, TasksUiAction, TasksUiEvent>(),
    TasksViewModelDelegate by tasksViewModelDelegate {

    /**
     * Hold the current selected tag for filtering
     */
    var selectedTag = MutableStateFlow("")
        private set

    val pomodoroState = timerManager.pomodoroTimerState

    override fun initState(): UiState<TasksUiModel> = UiState.Loading

    init {
        submitAction(Load)
    }

    override fun handleAction(action: TasksUiAction) {
        when (action) {
            Load -> loadTasksWithTags()
            is TagPressed -> selectedTag.value = action.tagId
            is StartRunningTaskPressed -> startRunningTask(action.task)
            is TaskCompleted -> setTaskCompleted(action.task)
            is UpdateDueDatePressed -> updateDueDatePressed()
            UpdateDueDateDismissed -> updateDueDateDismissed()
            is UpdatedDueDateConfirmed -> {
                updateDueDateConfirmed(action.offsetDateTime)
                submitSingleEvent(TasksUiEvent.DueDateUpdated)
            }
            is UpdatePomodoroPressed -> updatePomodoroPressed()
            UpdatePomodoroDismissed -> updatePomodoroDismissed()
            is UpdatePomodoroConfirmed -> {
                updatePomodoroConfirmed(action.pomodoro)
                submitSingleEvent(TasksUiEvent.PomodoroUpdated)
            }
            is DeleteTaskPressed -> deleteTaskPressed()
            DeleteTaskDismissed -> deleteTaskDismissed()
            DeleteTaskConfirmed -> {
                deleteTaskConfirmed()
                submitSingleEvent(TasksUiEvent.TaskDeleted)
            }
            is NavigateToTask -> submitSingleEvent(TasksUiEvent.NavigateToTask(action.taskId))
            is NavigateToRelatedTasks -> {
                submitSingleEvent(TasksUiEvent.NavigateToRelatedTasks(action.deadlineType))
            }
        }
    }

    private fun startRunningTask(task: Task) {
        viewModelScope.launch {
            val timerState = pomodoroState.first().timerState
            if (timerState == TimerState.RUNNING) {
                submitSingleEvent(TasksUiEvent.TaskIsAlreadyRunning)
            } else {
                timerManager.updatePomodoroStateManagerAndStartTimer(task)
                submitSingleEvent(TasksUiEvent.NavigateToTimer(task.id))
            }
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun loadTasksWithTags() {
        viewModelScope.launch {
            selectedTag
                .flatMapLatest { tagId ->
                    getTasksWithTagsUseCase.execute(
                        GetTasksWithTagsUseCase.Request(
                            filter = Filter.TagFilter(
                                filterId = tagId,
                                grouping = Grouping.DeadlineTypeGrouping
                            ),
                            taskCompleted = false
                        )
                    )
                }
                .collectLatest { result ->
                    submitState(converter.convert(result))
                }
        }
    }
}