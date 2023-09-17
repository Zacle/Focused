package com.zn.apps.ui_common.delegate

import com.zn.apps.common.network.di.ApplicationScope
import com.zn.apps.domain.project.GetProjectsUseCase
import com.zn.apps.domain.tag.GetTagsUseCase
import com.zn.apps.domain.task.DeleteTaskUseCase
import com.zn.apps.domain.task.UpsertTaskUseCase
import com.zn.apps.model.data.task.Pomodoro
import com.zn.apps.model.data.task.Task
import com.zn.apps.model.usecase.Result
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.OffsetDateTime
import javax.inject.Inject

interface TasksViewModelDelegate {

    val tasksUiStateHolder: StateFlow<TasksUiStateHolder>

    /** set the task completed */
    fun setTaskCompleted(task: Task)

    /** flag the task that we intend to update the due date */
    fun updateDueDatePressed(task: Task)

    /** Ignore the task that has been flagged */
    fun updateDueDateDismissed()

    /**
     * Update the due date of the task that was flagged
     *
     * @return [Result] if the update was successful or unsuccessful to notify the user
     */
    fun updateDueDateConfirmed(offsetDateTime: OffsetDateTime?)

    /** flag the task that we intend to update the pomodoro */
    fun updatePomodoroPressed(task: Task)

    /** Ignore the task that has been flagged */
    fun updatePomodoroDismissed()

    /**
     * Update the pomodoro of the task that was flagged
     *
     * @return [Result] if the update was successful or unsuccessful to notify the user
     */
    fun updatePomodoroConfirmed(pomodoro: Pomodoro)

    /** flag the task that we intend to update to delete */
    fun deleteTaskPressed(task: Task)

    /** Ignore the task that has been flagged */
    fun deleteTaskDismissed()

    /**
     * Delete the task that was flagged
     *
     * @return [Result] if the delete was successful or unsuccessful to notify the user
     */
    fun deleteTaskConfirmed()

    /**
     * Insert or modify the task
     */
    fun upsertTask(task: Task)

    /**
     * Record the task expand to show options
     */
    fun expand(task: Task)

    /**
     * Remove the recorded task
     */
    fun collapse()
}

class DefaultTasksViewModelDelegate @Inject constructor(
    private val upsertTaskUseCase: UpsertTaskUseCase,
    private val deleteTaskUseCase: DeleteTaskUseCase,
    private val getTagsUseCase: GetTagsUseCase,
    private val getProjectsUseCase: GetProjectsUseCase,
    @ApplicationScope val scope: CoroutineScope
): TasksViewModelDelegate {

    /**
     * State Holder for screen dialogs
     */
    override val tasksUiStateHolder = MutableStateFlow(TasksUiStateHolder())

    init {
        scope.launch {
            combine(
                getProjectsUseCase.execute(GetProjectsUseCase.Request),
                getTagsUseCase.execute(GetTagsUseCase.Request)
            ) { projectsResult, tagsResult ->
                Pair(projectsResult, tagsResult)
            }.collectLatest { pair ->
                if (pair.first is Result.Success) {
                    tasksUiStateHolder.update {
                        it.copy(
                            projects = (pair.first as Result.Success<GetProjectsUseCase.Response>).data.projects
                        )
                    }
                }
                if (pair.second is Result.Success) {
                    tasksUiStateHolder.update {
                        it.copy(
                            tags = (pair.second as Result.Success<GetTagsUseCase.Response>).data.tags
                        )
                    }
                }
            }
        }
    }

    private fun upsert(task: Task) {
        scope.launch {
            upsertTaskUseCase.execute(
                UpsertTaskUseCase.Request(
                    task = task
                )
            )
        }
    }

    override fun setTaskCompleted(task: Task) {
        upsert(task.copy(completed = true, completedTime = OffsetDateTime.now()))
    }

    override fun updateDueDatePressed(task: Task) {
        tasksUiStateHolder.update {
            it.copy(showDueDateDialog = true, taskPressed = task)
        }
    }

    override fun updateDueDateDismissed() {
        tasksUiStateHolder.update {
            it.copy(showDueDateDialog = false, taskPressed = null)
        }
    }

    override fun updateDueDateConfirmed(offsetDateTime: OffsetDateTime?) {
        val task = tasksUiStateHolder.value.taskPressed
        if (task != null) {
            upsert(task.copy(dueDate = offsetDateTime))
        }
        tasksUiStateHolder.update {
            it.copy(showDueDateDialog = false, taskPressed = null)
        }
    }

    override fun updatePomodoroPressed(task: Task) {
        tasksUiStateHolder.update {
            it.copy(showPomodoroDialog = true, taskPressed = task)
        }
    }

    override fun updatePomodoroDismissed() {
        tasksUiStateHolder.update {
            it.copy(showPomodoroDialog = false, taskPressed = null)
        }
    }

    override fun updatePomodoroConfirmed(pomodoro: Pomodoro) {
        val task = tasksUiStateHolder.value.taskPressed
        if (task != null) {
            upsert(task.copy(pomodoro = pomodoro))
        }
        tasksUiStateHolder.update {
            it.copy(showPomodoroDialog = false, taskPressed = null)
        }
    }

    override fun deleteTaskPressed(task: Task) {
        tasksUiStateHolder.update {
            it.copy(showDeleteTaskDialog = true, taskPressed = task)
        }
    }

    override fun deleteTaskDismissed() {
        tasksUiStateHolder.update {
            it.copy(showDeleteTaskDialog = false, taskPressed = null)
        }
    }

    override fun deleteTaskConfirmed() {
        val task = tasksUiStateHolder.value.taskPressed
        if (task != null) {
            scope.launch {
                deleteTaskUseCase.execute(
                    DeleteTaskUseCase.Request(
                        task = task
                    )
                )
            }
        }
        tasksUiStateHolder.update {
            it.copy(showDeleteTaskDialog = false, taskPressed = null)
        }
    }

    override fun upsertTask(task: Task) {
        upsert(task)
    }

    override fun expand(task: Task) {
        tasksUiStateHolder.update {
            it.copy(taskPressed = task)
        }
    }

    override fun collapse() {
        tasksUiStateHolder.update {
            it.copy(taskPressed = null)
        }
    }
}