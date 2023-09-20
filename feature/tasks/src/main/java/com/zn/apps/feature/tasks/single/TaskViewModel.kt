package com.zn.apps.feature.tasks.single

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.zn.apps.domain.task.GetTaskUseCase
import com.zn.apps.feature.tasks.single.TaskUiAction.DeleteTaskConfirmed
import com.zn.apps.feature.tasks.single.TaskUiAction.DeleteTaskDismissed
import com.zn.apps.feature.tasks.single.TaskUiAction.DeleteTaskPressed
import com.zn.apps.feature.tasks.single.TaskUiAction.Load
import com.zn.apps.feature.tasks.single.TaskUiAction.NavigateBackPressed
import com.zn.apps.feature.tasks.single.TaskUiAction.UpdatePressed
import com.zn.apps.ui_common.delegate.TasksViewModelDelegate
import com.zn.apps.ui_common.state.BaseViewModel
import com.zn.apps.ui_common.state.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

const val TASK_ID_ARGUMENT = "taskId"

@HiltViewModel
class TaskViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getTaskUseCase: GetTaskUseCase,
    private val tasksViewModelDelegate: TasksViewModelDelegate,
    private val converter: TaskUiConverter
): BaseViewModel<TaskUiModel, UiState<TaskUiModel>, TaskUiAction, TaskUiEvent>(),
    TasksViewModelDelegate by tasksViewModelDelegate {

    /** id parameter passed to the route **/
    private val taskId: String = requireNotNull(savedStateHandle[TASK_ID_ARGUMENT])

    override fun initState(): UiState<TaskUiModel> = UiState.Loading

    init {
        submitAction(Load)
    }

    override fun handleAction(action: TaskUiAction) {
        when(action) {
            Load -> loadTask()
            is UpdatePressed -> {
                upsertTask(action.task)
                submitSingleEvent(TaskUiEvent.TaskUpdated)
            }
            is DeleteTaskPressed -> deleteTaskPressed()
            is DeleteTaskDismissed -> deleteTaskDismissed()
            is DeleteTaskConfirmed -> {
                expand(action.task)
                deleteTaskConfirmed()
                submitSingleEvent(TaskUiEvent.NavigateBack)
            }
            NavigateBackPressed -> submitSingleEvent(TaskUiEvent.NavigateBack)
        }
    }

    private fun loadTask() {
        viewModelScope.launch {
            getTaskUseCase.execute(
                GetTaskUseCase.Request(taskId)
            ).collectLatest { result ->
                submitState(converter.convert(result))
            }
        }
    }
}