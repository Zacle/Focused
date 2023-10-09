package com.zn.apps.feature.tags

data class TagsUiStateHolder(
    val searchQuery: String = "",
    val exactSearchMatchFound: Boolean = false,
    val loading: Boolean = false
)
