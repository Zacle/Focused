package com.zn.apps.ui_common.delegate

import com.zn.apps.model.data.project.Project

data class ProjectsUiStateHolder(
    val showCompleteProjectDialog: Boolean = false,
    val showDeleteProjectDialog: Boolean = false,
    val isSearching: Boolean = false,
    val projectPressed: Project? = null
)
