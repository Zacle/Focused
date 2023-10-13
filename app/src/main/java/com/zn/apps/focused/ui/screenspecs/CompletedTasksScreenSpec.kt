package com.zn.apps.focused.ui.screenspecs

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavBackStackEntry
import com.zn.apps.feature.tasks.R
import com.zn.apps.feature.tasks.completed.CompletedTasksRoute
import com.zn.apps.feature.tasks.completed.CompletedTasksUiAction
import com.zn.apps.feature.tasks.completed.CompletedTasksUiEvent
import com.zn.apps.feature.tasks.completed.CompletedTasksViewModel
import com.zn.apps.focused.ui.FocusedAppState
import com.zn.apps.ui_common.state.CommonScreen
import com.zn.apps.ui_design.icon.FAIcons
import com.zn.apps.ui_design.icon.Icon
import kotlinx.coroutines.flow.collectLatest

data object CompletedTasksScreenSpec: NavigationDrawerScreenSpec {

    override val icon: Icon = Icon.DrawableResourceIcon(FAIcons.completed_tasks)

    override val label: Int = R.string.completed_tasks

    override val route: String = "completed_tasks"

    @Composable
    override fun Content(appState: FocusedAppState, navBackStackEntry: NavBackStackEntry) {
        val viewModel: CompletedTasksViewModel = hiltViewModel()

        LaunchedEffect(Unit) {
            viewModel.submitAction(CompletedTasksUiAction.Load)
        }

        val selectedTagId by viewModel.selectedTag.collectAsStateWithLifecycle()

        viewModel.uiStateFlow.collectAsStateWithLifecycle().value.let { state ->
            CommonScreen(state = state) { completedTasksUiModel ->
                CompletedTasksRoute(
                    completedTasksUiModel = completedTasksUiModel,
                    selectedTagId = selectedTagId,
                    onTaskPressed = {
                        viewModel.submitAction(CompletedTasksUiAction.NavigateToTask(it))
                    },
                    onSetTaskUnCompleted = {
                        viewModel.submitAction(CompletedTasksUiAction.SetTaskUnCompleted(it))
                    },
                    onTagPressed = {
                        viewModel.submitAction(CompletedTasksUiAction.TagPressed(it))
                    },
                    onUpPressed = { appState.navController.navigateUp() }
                )
            }
        }

        LaunchedEffect(Unit) {
            viewModel.singleEventFlow.collectLatest {
                when(it) {
                    is CompletedTasksUiEvent.NavigateToTask -> {
                        appState.navController.navigate(TaskScreenSpec.buildRoute(it.taskId))
                    }
                }
            }
        }
    }
}