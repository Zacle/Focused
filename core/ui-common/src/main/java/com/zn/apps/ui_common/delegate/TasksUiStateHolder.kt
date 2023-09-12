package com.zn.apps.ui_common.delegate

import com.zn.apps.model.data.project.Project
import com.zn.apps.model.data.tag.Tag
import com.zn.apps.model.data.task.Task

data class TasksUiStateHolder(
    val showCompleteTaskDialog: Boolean = false,
    val showDueDateDialog: Boolean = false,
    val showPomodoroDialog: Boolean = false,
    val showDeleteTaskDialog: Boolean = false,
    val taskPressed: Task? = null,
    val projects: List<Project> = emptyList(),
    val tags: List<Tag> = emptyList()
)
