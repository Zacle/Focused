package com.zn.apps.ui_common.related_tasks

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.zn.apps.domain.project.GetProjectTasksWithMetadata
import com.zn.apps.filter.Filter
import com.zn.apps.filter.Grouping
import com.zn.apps.filter.Grouping.TagGrouping
import com.zn.apps.ui_common.delegate.TasksViewModelDelegate
import com.zn.apps.ui_common.related_tasks.RelatedTasksUiAction.DeleteTaskConfirmed
import com.zn.apps.ui_common.related_tasks.RelatedTasksUiAction.DeleteTaskDismissed
import com.zn.apps.ui_common.related_tasks.RelatedTasksUiAction.DeleteTaskPressed
import com.zn.apps.ui_common.related_tasks.RelatedTasksUiAction.Load
import com.zn.apps.ui_common.related_tasks.RelatedTasksUiAction.NavigateToTask
import com.zn.apps.ui_common.related_tasks.RelatedTasksUiAction.SetGrouping
import com.zn.apps.ui_common.related_tasks.RelatedTasksUiAction.TaskCompleted
import com.zn.apps.ui_common.related_tasks.RelatedTasksUiAction.UpdateDueDateConfirmed
import com.zn.apps.ui_common.related_tasks.RelatedTasksUiAction.UpdateDueDateDismissed
import com.zn.apps.ui_common.related_tasks.RelatedTasksUiAction.UpdateDueDatePressed
import com.zn.apps.ui_common.related_tasks.RelatedTasksUiAction.UpdatePomodoroConfirmed
import com.zn.apps.ui_common.related_tasks.RelatedTasksUiAction.UpdatePomodoroDismissed
import com.zn.apps.ui_common.related_tasks.RelatedTasksUiAction.UpdatePomodoroPressed
import com.zn.apps.ui_common.state.BaseViewModel
import com.zn.apps.ui_common.state.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

const val PROJECT_ID_ARGUMENT = "projectId"

@HiltViewModel
class ProjectWithTasksViewModel @Inject constructor(
    private val getProjectTasksWithMetadata: GetProjectTasksWithMetadata,
    private val converter: RelatedTasksUiConverter,
    tasksViewModelDelegate: TasksViewModelDelegate,
    savedStateHandle: SavedStateHandle
): BaseViewModel<RelatedTasksUiModel, UiState<RelatedTasksUiModel>, RelatedTasksUiAction, RelatedTasksUiEvent>(),
    TasksViewModelDelegate by tasksViewModelDelegate {

    /** id parameter passed to the route **/
    private val projectId: String = requireNotNull(savedStateHandle[PROJECT_ID_ARGUMENT])

    /**
     * Hold the current grouping type
     */
    var groupingType = MutableStateFlow<Grouping>(TagGrouping)
        private set

    override fun initState(): UiState<RelatedTasksUiModel> = UiState.Loading

    init {
        submitAction(Load)
    }

    override fun handleAction(action: RelatedTasksUiAction) {
        when(action) {
            Load -> loadMetadataAndTasks()
            is SetGrouping -> groupingType.value = action.grouping
            is TaskCompleted -> setTaskCompleted(action.task)
            UpdateDueDatePressed -> updateDueDatePressed()
            UpdateDueDateDismissed -> updateDueDateDismissed()
            is UpdateDueDateConfirmed -> {
                updateDueDateConfirmed(action.offsetDateTime)
                submitSingleEvent(RelatedTasksUiEvent.DueDateUpdated)
            }
            UpdatePomodoroPressed -> updatePomodoroPressed()
            UpdatePomodoroDismissed -> updatePomodoroDismissed()
            is UpdatePomodoroConfirmed -> {
                updatePomodoroConfirmed(action.pomodoro)
                submitSingleEvent(RelatedTasksUiEvent.PomodoroUpdated)
            }
            DeleteTaskPressed -> deleteTaskPressed()
            DeleteTaskDismissed -> deleteTaskDismissed()
            DeleteTaskConfirmed -> {
                deleteTaskConfirmed()
                submitSingleEvent(RelatedTasksUiEvent.TaskDeleted)
            }
            is NavigateToTask -> {
                submitSingleEvent(RelatedTasksUiEvent.NavigateToTask(action.taskId))
            }
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun loadMetadataAndTasks() {
        viewModelScope.launch {
            groupingType
                .flatMapLatest { type ->
                    getProjectTasksWithMetadata.execute(
                        GetProjectTasksWithMetadata.Request(
                            filter = Filter.ProjectFilter(projectId, type),
                            projectId = projectId,
                            isTaskCompleted = false
                        )
                    )
                }
                .collectLatest { result ->
                    submitState(converter.convert(result))
                }
        }
    }
}