package com.zn.apps.feature.tasks.completed

import com.zn.apps.ui_common.state.UiEvent

sealed class CompletedTasksUiEvent: UiEvent {
    data class NavigateToTask(val taskId: String): CompletedTasksUiEvent()
}
