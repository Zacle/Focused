package com.zn.apps.feature.tags

import com.zn.apps.ui_common.state.UiEvent

sealed class TagsUiEvent: UiEvent {
    data object TagDeleted: TagsUiEvent()
    data object TagUpdated: TagsUiEvent()
}