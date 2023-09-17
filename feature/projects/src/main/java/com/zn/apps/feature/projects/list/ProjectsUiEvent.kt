package com.zn.apps.feature.projects.list

import com.zn.apps.ui_common.state.UiEvent

sealed class ProjectsUiEvent: UiEvent {
    data object ProjectDeleted: ProjectsUiEvent()
    data class NavigateToRelatedTasks(val projectId: String): ProjectsUiEvent()
    data class NavigateToEditProject(val projectId: String): ProjectsUiEvent()
}