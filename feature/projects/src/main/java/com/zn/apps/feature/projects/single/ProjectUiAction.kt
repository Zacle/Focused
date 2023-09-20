package com.zn.apps.feature.projects.single

import com.zn.apps.model.data.project.Project
import com.zn.apps.ui_common.state.UiAction

sealed class ProjectUiAction: UiAction {
    data object Load: ProjectUiAction()
    data class UpdatePressed(val project: Project): ProjectUiAction()
    data object DeleteProjectPressed: ProjectUiAction()
    data object DeleteProjectDismissed: ProjectUiAction()
    data class DeleteProjectConfirmed(val project: Project): ProjectUiAction()
    data object NavigateBackPressed: ProjectUiAction()
}
