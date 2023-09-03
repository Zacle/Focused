package com.zn.apps.feature.tasks.list

import com.zn.apps.model.data.task.Pomodoro
import com.zn.apps.model.data.task.Task
import com.zn.apps.ui_common.state.UiAction
import java.time.OffsetDateTime

sealed class TasksUiAction: UiAction {
    data object Load: TasksUiAction()
    data class TagPressed(val tagId: String): TasksUiAction()
    data class TaskCompleted(val task: Task): TasksUiAction()
    data class UpdateDueDatePressed(val task: Task): TasksUiAction()
    data class UpdatedDueDateConfirmed(val offsetDateTime: OffsetDateTime?): TasksUiAction()
    data object UpdateDueDateDismissed: TasksUiAction()
    data class UpdatePomodoroPressed(val task: Task): TasksUiAction()
    data class UpdatePomodoroConfirmed(val pomodoro: Pomodoro): TasksUiAction()
    data object UpdatePomodoroDismissed: TasksUiAction()
    data class DeleteTaskPressed(val task: Task): TasksUiAction()
    data object DeleteTaskConfirmed: TasksUiAction()
    data object DeleteTaskDismissed: TasksUiAction()
    data class NavigateToTask(val taskId: String): TasksUiAction()
    data class NavigateToRelatedTasks(val deadlineType: Int): TasksUiAction()
}