package com.zn.apps.ui_common.delegate

import com.zn.apps.model.data.task.Task

data class TasksUiStateHolder(
    val showCompleteTaskDialog: Boolean = false,
    val showDueDateDialog: Boolean = false,
    val showPomodoroDialog: Boolean = false,
    val showDeleteTaskDialog: Boolean = false,
    val taskPressed: Task? = null
)
