package com.zn.apps.feature.tasks.list

import androidx.lifecycle.viewModelScope
import com.zn.apps.domain.GetTasksWithTagsUseCase
import com.zn.apps.feature.tasks.list.TasksUiAction.DeleteTaskConfirmed
import com.zn.apps.feature.tasks.list.TasksUiAction.DeleteTaskDismissed
import com.zn.apps.feature.tasks.list.TasksUiAction.DeleteTaskPressed
import com.zn.apps.feature.tasks.list.TasksUiAction.Load
import com.zn.apps.feature.tasks.list.TasksUiAction.NavigateToRelatedTasks
import com.zn.apps.feature.tasks.list.TasksUiAction.NavigateToTask
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
import com.zn.apps.model.usecase.Result.Error
import com.zn.apps.model.usecase.Result.Success
import com.zn.apps.ui_common.delegate.TasksViewModelDelegate
import com.zn.apps.ui_common.state.BaseViewModel
import com.zn.apps.ui_common.state.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TasksViewModel @Inject constructor(
    private val getTasksWithTagsUseCase: GetTasksWithTagsUseCase,
    private val tasksViewModelDelegate: TasksViewModelDelegate,
    private val converter: TasksUiConverter
): BaseViewModel<TasksUiModel, UiState<TasksUiModel>, TasksUiAction, TasksUiEvent>(),
    TasksViewModelDelegate by tasksViewModelDelegate {

    /**
     * Hold the current selected tag for filtering
     */
    var selectedTag  = MutableStateFlow("")
        private set

    override fun initState(): UiState<TasksUiModel> = UiState.Loading

    init {
        submitAction(Load)
    }

    override fun handleAction(action: TasksUiAction) {
        when (action) {
            Load -> loadTasksWithTags()
            is TagPressed -> selectedTag.value = action.tagId
            is TaskCompleted -> {
                when(setTaskCompleted(action.task)) {
                    is Success -> submitSingleEvent(TasksUiEvent.DueDateUpdated)
                    is Error -> submitSingleEvent(TasksUiEvent.TaskNotUpdated)
                    else -> {}
                }
            }
            is UpdateDueDatePressed -> updateDueDatePressed(action.task)
            UpdateDueDateDismissed -> updateDueDateDismissed()
            is UpdatedDueDateConfirmed -> {
                when(updateDueDateConfirmed(action.offsetDateTime)) {
                    is Success -> submitSingleEvent(TasksUiEvent.DueDateUpdated)
                    is Error -> submitSingleEvent(TasksUiEvent.TaskNotUpdated)
                    else -> {}
                }
            }
            is UpdatePomodoroPressed -> updatePomodoroPressed(action.task)
            UpdatePomodoroDismissed -> updatePomodoroDismissed()
            is UpdatePomodoroConfirmed -> {
                when(updatePomodoroConfirmed(action.pomodoro)) {
                    is Success -> submitSingleEvent(TasksUiEvent.PomodoroUpdated)
                    is Error -> submitSingleEvent(TasksUiEvent.TaskNotUpdated)
                    else -> {}
                }
            }
            is DeleteTaskPressed -> deleteTaskPressed(action.task)
            DeleteTaskDismissed -> deleteTaskDismissed()
            DeleteTaskConfirmed -> {
                when(deleteTaskConfirmed()) {
                    is Success -> submitSingleEvent(TasksUiEvent.TaskDeleted)
                    is Error -> submitSingleEvent(TasksUiEvent.TaskNotDeleted)
                    else -> {}
                }
            }
            is NavigateToTask -> submitSingleEvent(TasksUiEvent.NavigateToTask(action.taskId))
            is NavigateToRelatedTasks -> {
                submitSingleEvent(TasksUiEvent.NavigateToRelatedTasks(action.deadlineType))
            }
        }
    }

    private fun loadTasksWithTags() {
        viewModelScope.launch {
            combine(
                getTasksWithTagsUseCase.execute(
                    GetTasksWithTagsUseCase.Request(
                        filter = Filter.TagFilter(
                            filterId = selectedTag.value,
                            grouping = Grouping.DeadlineTypeGrouping
                        ),
                        taskCompleted = false
                    )
                ),
                selectedTag
            ) { result, _ ->
                converter.convert(result)
            }.collectLatest { state ->
                submitState(state)
            }
        }
    }
}