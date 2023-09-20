package com.zn.apps.feature.tasks.single

import com.zn.apps.ui_common.state.UiEvent

sealed class TaskUiEvent: UiEvent {
    data object TaskUpdated: TaskUiEvent()
    data object NavigateBack: TaskUiEvent()
}
