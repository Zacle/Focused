package com.zn.apps.feature.tasks.completed

import com.zn.apps.model.data.task.Task
import com.zn.apps.ui_common.state.UiAction

sealed class CompletedTasksUiAction: UiAction {
    data object Load: CompletedTasksUiAction()
    data class TagPressed(val tagId: String): CompletedTasksUiAction()
    data class SetTaskUnCompleted(val task: Task): CompletedTasksUiAction()
    data class NavigateToTask(val taskId: String): CompletedTasksUiAction()
}
