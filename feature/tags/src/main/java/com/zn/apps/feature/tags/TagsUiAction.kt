package com.zn.apps.feature.tags

import com.zn.apps.model.data.tag.Tag
import com.zn.apps.ui_common.state.UiAction

sealed class TagsUiAction: UiAction {

    data object Load: TagsUiAction()
    data class SearchTag(val query: String): TagsUiAction()
    data class UpsertTag(val tag: Tag): TagsUiAction()
    data class DeleteTag(val tag: Tag): TagsUiAction()
}
