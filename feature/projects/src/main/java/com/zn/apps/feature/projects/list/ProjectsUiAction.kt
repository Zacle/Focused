package com.zn.apps.feature.projects.list

import com.zn.apps.model.data.project.ProjectFilterType
import com.zn.apps.ui_common.state.UiAction

sealed class ProjectsUiAction: UiAction {
    data object Load: ProjectsUiAction()
    data class SetProjectFilterType(val filterType: ProjectFilterType): ProjectsUiAction()
    data class UpdateProjectQuery(val query: String): ProjectsUiAction()
    data class NavigateToRelatedTasks(val projectId: String): ProjectsUiAction()
    data class NavigateToEditProject(val projectId: String): ProjectsUiAction()
    data object SearchProjectPressed: ProjectsUiAction()
    data object SearchProjectDismissed: ProjectsUiAction()
    data object DeleteProjectPressed: ProjectsUiAction()
    data object DeleteProjectDismissed: ProjectsUiAction()
    data object DeleteProjectConfirmed: ProjectsUiAction()
    data object CompleteProjectPressed: ProjectsUiAction()
    data object CompleteProjectDismissed: ProjectsUiAction()
    data object CompleteProjectConfirmed: ProjectsUiAction()
}
