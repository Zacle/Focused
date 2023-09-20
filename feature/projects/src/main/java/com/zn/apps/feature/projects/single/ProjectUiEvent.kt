package com.zn.apps.feature.projects.single

import com.zn.apps.ui_common.state.UiEvent

sealed class ProjectUiEvent: UiEvent {
    data object ProjectUpdated: ProjectUiEvent()
    data object NavigateBack: ProjectUiEvent()
}
