package com.zn.apps.ui_common.related_tasks

import com.zn.apps.filter.Grouping
import com.zn.apps.model.data.task.Pomodoro
import com.zn.apps.model.data.task.Task
import com.zn.apps.ui_common.state.UiAction
import java.time.OffsetDateTime

sealed class RelatedTasksUiAction: UiAction {
    data object Load: RelatedTasksUiAction()
    data class SetGrouping(val grouping: Grouping): RelatedTasksUiAction()
    data class StartRunningTaskPressed(val task: Task): RelatedTasksUiAction()
    data class TaskCompleted(val task: Task): RelatedTasksUiAction()
    data object UpdateDueDatePressed: RelatedTasksUiAction()
    data class UpdateDueDateConfirmed(
        val offsetDateTime: OffsetDateTime?,
        val remindTaskAt: Int,
        val isReminderSet: Boolean
    ): RelatedTasksUiAction()
    data object UpdateDueDateDismissed: RelatedTasksUiAction()
    data object UpdatePomodoroPressed: RelatedTasksUiAction()
    data class UpdatePomodoroConfirmed(val pomodoro: Pomodoro): RelatedTasksUiAction()
    data object UpdatePomodoroDismissed: RelatedTasksUiAction()
    data object DeleteTaskPressed: RelatedTasksUiAction()
    data object DeleteTaskConfirmed: RelatedTasksUiAction()
    data object DeleteTaskDismissed: RelatedTasksUiAction()
    data class NavigateToTask(val taskId: String): RelatedTasksUiAction()
}
