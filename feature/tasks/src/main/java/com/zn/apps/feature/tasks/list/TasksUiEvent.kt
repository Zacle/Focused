package com.zn.apps.feature.tasks.list

import com.zn.apps.ui_common.state.UiEvent

sealed class TasksUiEvent: UiEvent {

    data object TaskNotUpdated: TasksUiEvent()
    data object TaskDeleted: TasksUiEvent()
    data object TaskNotDeleted: TasksUiEvent()
    data object DueDateUpdated: TasksUiEvent()
    data object PomodoroUpdated: TasksUiEvent()
    data class NavigateToTask(val taskId: String): TasksUiEvent()
    data class NavigateToRelatedTasks(val deadlineType: Int): TasksUiEvent()
}