package com.zn.apps.focused.ui.screenspecs

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavBackStackEntry
import com.zn.apps.feature.tags.TagsRoute
import com.zn.apps.feature.tags.TagsUiAction
import com.zn.apps.feature.tags.TagsUiEvent
import com.zn.apps.feature.tags.TagsViewModel
import com.zn.apps.focused.R
import com.zn.apps.focused.ui.FocusedAppState
import com.zn.apps.ui_common.state.CommonScreen
import com.zn.apps.ui_design.icon.FAIcons
import com.zn.apps.ui_design.icon.Icon
import com.zn.apps.ui_design.icon.Icon.DrawableResourceIcon
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import com.zn.apps.feature.tags.R as tagsR

data object TagsScreenSpec: NavigationDrawerScreenSpec {

    override val icon: Icon = DrawableResourceIcon(id = FAIcons.tag)

    override val label: Int = tagsR.string.tags

    override val route: String = "tags"

    @Composable
    override fun Content(appState: FocusedAppState, navBackStackEntry: NavBackStackEntry) {
        val viewModel: TagsViewModel = hiltViewModel()

        LaunchedEffect(Unit) {
            viewModel.submitAction(TagsUiAction.Load)
        }

        val uiStateHolder by viewModel.uiStateHolder.collectAsStateWithLifecycle()

        viewModel.uiStateFlow.collectAsStateWithLifecycle().value.let { state ->
            CommonScreen(state = state) { tagsUiModel ->
                TagsRoute(
                    tagsUiModel = tagsUiModel,
                    uiStateHolder = uiStateHolder,
                    onSearchTag = {
                        viewModel.submitAction(TagsUiAction.SearchTag(it))
                    },
                    onUpsertTag = {
                        viewModel.submitAction(TagsUiAction.UpsertTag(it))
                    },
                    onDeleteTag = {
                        viewModel.submitAction(TagsUiAction.DeleteTag(it))
                    },
                    upPress = { appState.navController.navigateUp() }
                )
            }
        }

        val context = LocalContext.current

        LaunchedEffect(Unit) {
            viewModel.singleEventFlow.collectLatest {
                when(it) {
                    TagsUiEvent.TagUpdated -> {
                        showSnackbar(appState, context.getString(R.string.tag_updated))
                    }
                    TagsUiEvent.TagDeleted -> {
                        showSnackbar(appState, context.getString(R.string.tag_deleted))
                    }
                }
            }
        }
    }

    private suspend fun showSnackbar(appState: FocusedAppState, message: String) {
        appState.coroutineScope.launch {
            appState.snackbarHostState.showSnackbar(
                message = message,
                withDismissAction = true
            )
        }
    }
}