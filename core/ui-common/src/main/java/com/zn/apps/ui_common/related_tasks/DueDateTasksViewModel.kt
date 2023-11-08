package com.zn.apps.ui_common.related_tasks

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.zn.apps.common.DeadlineType
import com.zn.apps.core.timer.TimerManager
import com.zn.apps.domain.repository.ReminderPreferencesRepository
import com.zn.apps.domain.task.GetTasksWithMetadataUseCase
import com.zn.apps.filter.Filter
import com.zn.apps.filter.Grouping
import com.zn.apps.model.data.pomodoro.TimerState
import com.zn.apps.model.data.task.Task
import com.zn.apps.ui_common.delegate.TasksViewModelDelegate
import com.zn.apps.ui_common.related_tasks.RelatedTasksUiAction.DeleteTaskConfirmed
import com.zn.apps.ui_common.related_tasks.RelatedTasksUiAction.DeleteTaskDismissed
import com.zn.apps.ui_common.related_tasks.RelatedTasksUiAction.DeleteTaskPressed
import com.zn.apps.ui_common.related_tasks.RelatedTasksUiAction.Load
import com.zn.apps.ui_common.related_tasks.RelatedTasksUiAction.NavigateToTask
import com.zn.apps.ui_common.related_tasks.RelatedTasksUiAction.SetGrouping
import com.zn.apps.ui_common.related_tasks.RelatedTasksUiAction.StartRunningTaskPressed
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
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

const val DEADLINE_TYPE_ARGUMENT = "deadlineType"

@HiltViewModel
class DueDateTasksViewModel @Inject constructor(
    private val getTasksWithMetadata: GetTasksWithMetadataUseCase,
    private val converter: DueDateTasksUiConverter,
    private val timerManager: TimerManager,
    tasksViewModelDelegate: TasksViewModelDelegate,
    reminderPreferencesRepository: ReminderPreferencesRepository,
    savedStateHandle: SavedStateHandle
): BaseViewModel<RelatedTasksUiModel, UiState<RelatedTasksUiModel>, RelatedTasksUiAction, RelatedTasksUiEvent>(),
    TasksViewModelDelegate by tasksViewModelDelegate {

    /** [deadlineTypeParam] is the argument passed in the composable to retrieve tasks
     * related to that due date
     */
    private val deadlineTypeParam: Int = checkNotNull(savedStateHandle[DEADLINE_TYPE_ARGUMENT])
    /** Get the actual value to filter data **/
    val deadlineType: DeadlineType = DeadlineType.values()[deadlineTypeParam]

    /**
     * Hold the current grouping type
     */
    var groupingType = MutableStateFlow<Grouping>(Grouping.TagGrouping)
        private set

    val pomodoroState = timerManager.pomodoroTimerState

    val reminderPreferences = reminderPreferencesRepository.reminderPreferences

    override fun initState(): UiState<RelatedTasksUiModel> = UiState.Loading

    init {
        submitAction(Load)
    }

    override fun handleAction(action: RelatedTasksUiAction) {
        when(action) {
            Load -> loadMetadataAndTasks()
            is SetGrouping -> groupingType.value = action.grouping
            is TaskCompleted -> setTaskCompleted(action.task)
            is StartRunningTaskPressed -> {
                startRunningTask(action.task)
            }
            UpdateDueDatePressed -> updateDueDatePressed()
            UpdateDueDateDismissed -> updateDueDateDismissed()
            is UpdateDueDateConfirmed -> {
                updateDueDateConfirmed(action.offsetDateTime, action.remindTaskAt, action.isReminderSet)
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

    private fun startRunningTask(task: Task) {
        viewModelScope.launch {
            val timerState = pomodoroState.first().timerState
            if (timerState == TimerState.RUNNING) {
                submitSingleEvent(RelatedTasksUiEvent.TaskIsAlreadyRunning)
            } else {
                timerManager.updatePomodoroStateManagerAndStartTimer(task)
                submitSingleEvent(RelatedTasksUiEvent.NavigateToTimer(task.id))
            }
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun loadMetadataAndTasks() {
        viewModelScope.launch {
            Timber.d("param = $deadlineTypeParam, actual = $deadlineType")
            groupingType
                .flatMapLatest { type ->
                    getTasksWithMetadata.execute(
                        GetTasksWithMetadataUseCase.Request(
                            filter = Filter.DateFilter(
                                dueDate = deadlineType,
                                grouping = type
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