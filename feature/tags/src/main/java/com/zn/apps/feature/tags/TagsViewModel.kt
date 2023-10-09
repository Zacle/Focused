package com.zn.apps.feature.tags

import androidx.lifecycle.viewModelScope
import com.zn.apps.domain.tag.DeleteTagUseCase
import com.zn.apps.domain.tag.SearchTagUseCase
import com.zn.apps.domain.tag.UpsertTagUseCase
import com.zn.apps.feature.tags.TagsUiAction.DeleteTag
import com.zn.apps.feature.tags.TagsUiAction.Load
import com.zn.apps.feature.tags.TagsUiAction.SearchTag
import com.zn.apps.feature.tags.TagsUiAction.UpsertTag
import com.zn.apps.model.data.tag.Tag
import com.zn.apps.ui_common.state.BaseViewModel
import com.zn.apps.ui_common.state.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TagsViewModel @Inject constructor(
    private val searchTagUseCase: SearchTagUseCase,
    private val upsertTagUseCase: UpsertTagUseCase,
    private val deleteTagUseCase: DeleteTagUseCase,
    private val converter: TagsUiConverter
): BaseViewModel<TagsUiModel, UiState<TagsUiModel>, TagsUiAction, TagsUiEvent>() {

    var uiStateHolder = MutableStateFlow(TagsUiStateHolder())

    private val tagNameToSearch = MutableStateFlow("")

    override fun initState(): UiState<TagsUiModel> = UiState.Loading

    init {
        submitAction(Load)
    }

    override fun handleAction(action: TagsUiAction) {
        when(action) {
            Load -> loadTags()
            is SearchTag -> searchTag(action.query)
            is UpsertTag -> upsertTag(action.tag)
            is DeleteTag -> deleteTag(action.tag)
        }
    }

    private fun deleteTag(tag: Tag) {
        viewModelScope.launch {
            deleteTagUseCase.execute(
                DeleteTagUseCase.Request(tag)
            )
            submitSingleEvent(TagsUiEvent.TagDeleted)
        }
    }

    private fun upsertTag(tag: Tag) {
        viewModelScope.launch {
            upsertTagUseCase.execute(
                UpsertTagUseCase.Request(tag)
            )
            submitSingleEvent(TagsUiEvent.TagUpdated)
        }
    }

    private fun searchTag(query: String) {
        tagNameToSearch.value = query
        uiStateHolder.update {
            it.copy(searchQuery = query)
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
    private fun loadTags() {
        viewModelScope.launch {
            tagNameToSearch
                .debounce(500L)
                .onEach { uiStateHolder.update { it.copy(loading = true) } }
                .flatMapLatest { query ->
                    searchTagUseCase.execute(
                        SearchTagUseCase.Request(query)
                    )
                }
                .onEach { uiStateHolder.update { it.copy(loading = false) } }
                .collectLatest { result ->
                    submitState(converter.convert(result))
                    val uiState = uiStateFlow.value
                    if (uiState is UiState.Success) {
                        uiStateHolder.update {
                            it.copy(
                                exactSearchMatchFound = uiState.data.tags.any { tag ->
                                    tag.name.lowercase() == uiStateHolder.value.searchQuery
                                }
                            )
                        }
                    }
                }
        }
    }
}