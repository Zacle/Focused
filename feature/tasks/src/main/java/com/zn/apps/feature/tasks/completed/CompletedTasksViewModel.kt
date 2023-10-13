package com.zn.apps.feature.tasks.completed

import androidx.lifecycle.viewModelScope
import com.zn.apps.domain.GetTasksWithTagsUseCase
import com.zn.apps.domain.task.UpsertTaskUseCase
import com.zn.apps.feature.tasks.completed.CompletedTasksUiAction.Load
import com.zn.apps.feature.tasks.completed.CompletedTasksUiAction.NavigateToTask
import com.zn.apps.feature.tasks.completed.CompletedTasksUiAction.SetTaskUnCompleted
import com.zn.apps.feature.tasks.completed.CompletedTasksUiAction.TagPressed
import com.zn.apps.filter.Filter
import com.zn.apps.filter.Grouping
import com.zn.apps.model.data.task.Task
import com.zn.apps.ui_common.state.BaseViewModel
import com.zn.apps.ui_common.state.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CompletedTasksViewModel @Inject constructor(
    private val getTasksWithTagsUseCase: GetTasksWithTagsUseCase,
    private val upsertTaskUseCase: UpsertTaskUseCase,
    private val converter: CompletedTasksUiConverter
): BaseViewModel<CompletedTasksUiModel, UiState<CompletedTasksUiModel>, CompletedTasksUiAction, CompletedTasksUiEvent>() {

    /**
     * Hold the current selected tag for filtering
     */
    var selectedTag = MutableStateFlow("")
        private set

    override fun initState(): UiState<CompletedTasksUiModel> = UiState.Loading

    init {
        submitAction(Load)
    }

    override fun handleAction(action: CompletedTasksUiAction) {
        when(action) {
            Load -> loadCompletedTasksWithTags()
            is TagPressed -> selectedTag.value = action.tagId
            is SetTaskUnCompleted -> setTaskUnCompleted(action.task)
            is NavigateToTask -> {
                submitSingleEvent(CompletedTasksUiEvent.NavigateToTask(action.taskId))
            }
        }
    }

    private fun setTaskUnCompleted(task: Task) {
        viewModelScope.launch {
            val taskToUpdate = task.copy(
                completed = false,
                completedTime = null
            )
            upsertTaskUseCase.execute(
                UpsertTaskUseCase.Request(taskToUpdate)
            )
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun loadCompletedTasksWithTags() {
        viewModelScope.launch {
            selectedTag
                .flatMapLatest { tagId ->
                    getTasksWithTagsUseCase.execute(
                        GetTasksWithTagsUseCase.Request(
                            filter = Filter.TagFilter(
                                filterId = tagId,
                                grouping = Grouping.DeadlineCompletedTimeGrouping
                            ),
                            taskCompleted = true
                        )
                    )
                }
                .collectLatest { result ->
                    submitState(converter.convert(result))
                }
        }
    }
}