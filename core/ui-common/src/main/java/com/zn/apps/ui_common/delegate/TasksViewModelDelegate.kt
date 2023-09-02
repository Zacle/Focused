package com.zn.apps.ui_common.delegate

import com.zn.apps.common.network.di.ApplicationScope
import com.zn.apps.domain.task.DeleteTaskUseCase
import com.zn.apps.domain.task.UpsertTaskUseCase
import com.zn.apps.model.data.task.Pomodoro
import com.zn.apps.model.data.task.Task
import com.zn.apps.model.usecase.Result
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.OffsetDateTime
import javax.inject.Inject

interface TasksViewModelDelegate {

    val tasksUiStateHolder: StateFlow<TasksUiStateHolder>

    /** set the task completed */
    fun setTaskCompleted(task: Task): Result<Any>?

    /** flag the task that we intend to update the due date */
    fun updateDueDatePressed(task: Task)

    /** Ignore the task that has been flagged */
    fun updateDueDateDismissed()

    /**
     * Update the due date of the task that was flagged
     *
     * @return [Result] if the update was successful or unsuccessful to notify the user
     */
    fun updateDueDateConfirmed(offsetDateTime: OffsetDateTime?): Result<Any>?

    /** flag the task that we intend to update the pomodoro */
    fun updatePomodoroPressed(task: Task)

    /** Ignore the task that has been flagged */
    fun updatePomodoroDismissed()

    /**
     * Update the pomodoro of the task that was flagged
     *
     * @return [Result] if the update was successful or unsuccessful to notify the user
     */
    fun updatePomodoroConfirmed(pomodoro: Pomodoro): Result<Any>?

    /** flag the task that we intend to update to delete */
    fun deleteTaskPressed(task: Task)

    /** Ignore the task that has been flagged */
    fun deleteTaskDismissed()

    /**
     * Delete the task that was flagged
     *
     * @return [Result] if the delete was successful or unsuccessful to notify the user
     */
    fun deleteTaskConfirmed(): Result<Any>?

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
    @ApplicationScope val scope: CoroutineScope
): TasksViewModelDelegate {

    /**
     * State Holder for screen dialogs
     */
    override val tasksUiStateHolder = MutableStateFlow(TasksUiStateHolder())

    private fun upsert(task: Task): Result<Any>? {
        var result: Result<Any>? = null
        scope.launch {
            upsertTaskUseCase.execute(
                UpsertTaskUseCase.Request(
                    task = task
                )
            ).collect {
                result = it
            }
        }
        return result
    }

    override fun setTaskCompleted(task: Task): Result<Any>? {
        return upsert(task.copy(completed = true, completedTime = OffsetDateTime.now()))
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

    override fun updateDueDateConfirmed(offsetDateTime: OffsetDateTime?): Result<Any>? {
        val task = tasksUiStateHolder.value.taskPressed
        var result: Result<Any>? = null
        if (task != null) {
            result = upsert(task.copy(dueDate = offsetDateTime))
        }
        tasksUiStateHolder.update {
            it.copy(showDueDateDialog = false, taskPressed = null)
        }
        return result
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

    override fun updatePomodoroConfirmed(pomodoro: Pomodoro): Result<Any>? {
        val task = tasksUiStateHolder.value.taskPressed
        var result: Result<Any>? = null
        if (task != null) {
            result = upsert(task.copy(pomodoro = pomodoro))
        }
        tasksUiStateHolder.update {
            it.copy(showPomodoroDialog = false, taskPressed = null)
        }
        return result
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

    override fun deleteTaskConfirmed(): Result<Any>? {
        val task = tasksUiStateHolder.value.taskPressed
        var result: Result<Any>? = null
        if (task != null) {
            scope.launch {
                deleteTaskUseCase.execute(
                    DeleteTaskUseCase.Request(
                        task = task
                    )
                ).collect {
                    result = it
                }
            }
        }
        tasksUiStateHolder.update {
            it.copy(showDeleteTaskDialog = false, taskPressed = null)
        }
        return result
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