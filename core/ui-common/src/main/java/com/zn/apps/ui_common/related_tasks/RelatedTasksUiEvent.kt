package com.zn.apps.ui_common.related_tasks

import com.zn.apps.ui_common.state.UiEvent

sealed class RelatedTasksUiEvent: UiEvent {
    data object TaskDeleted: RelatedTasksUiEvent()
    data object DueDateUpdated: RelatedTasksUiEvent()
    data object PomodoroUpdated: RelatedTasksUiEvent()
    data object TaskIsAlreadyRunning: RelatedTasksUiEvent()
    data class NavigateToTask(val taskId: String): RelatedTasksUiEvent()
    data class NavigateToTimer(val taskId: String): RelatedTasksUiEvent()
}
