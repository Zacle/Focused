package com.zn.apps.feature.tasks.single

import com.zn.apps.model.data.task.Task
import com.zn.apps.ui_common.state.UiAction

sealed class TaskUiAction: UiAction {
    data object Load: TaskUiAction()
    data class UpdatePressed(val task: Task): TaskUiAction()
    data object DeleteTaskPressed: TaskUiAction()
    data object DeleteTaskDismissed: TaskUiAction()
    data class DeleteTaskConfirmed(val task: Task): TaskUiAction()
    data object NavigateBackPressed: TaskUiAction()
}
