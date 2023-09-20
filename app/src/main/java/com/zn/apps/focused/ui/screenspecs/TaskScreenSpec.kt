package com.zn.apps.focused.ui.screenspecs

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.zn.apps.feature.tasks.single.TASK_ID_ARGUMENT
import com.zn.apps.feature.tasks.single.TaskRoute
import com.zn.apps.feature.tasks.single.TaskUiAction
import com.zn.apps.feature.tasks.single.TaskUiEvent
import com.zn.apps.feature.tasks.single.TaskViewModel
import com.zn.apps.focused.R
import com.zn.apps.focused.ui.FocusedAppState
import com.zn.apps.ui_common.state.CommonScreen
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

data object TaskScreenSpec: ScreenSpec {

    override val route: String = "tasks/{${TASK_ID_ARGUMENT}}"

    override val arguments: List<NamedNavArgument>
        get() = listOf(
            navArgument(TASK_ID_ARGUMENT) { type = NavType.StringType }
        )

    fun buildRoute(taskId: String) = "tasks/$taskId"

    @Composable
    override fun Content(appState: FocusedAppState, navBackStackEntry: NavBackStackEntry) {
        val viewModel: TaskViewModel = hiltViewModel()

        LaunchedEffect(Unit) {
            viewModel.submitAction(TaskUiAction.Load)
        }

        val uiStateHolder by viewModel.tasksUiStateHolder.collectAsStateWithLifecycle()

        viewModel.uiStateFlow.collectAsStateWithLifecycle().value.let { state ->
            CommonScreen(
                state = state
            ) { taskUiModel ->
                TaskRoute(
                    taskUiModel = taskUiModel,
                    uiStateHolder = uiStateHolder,
                    onUpdatePressed = {
                        viewModel.submitAction(TaskUiAction.UpdatePressed(it))
                    },
                    onDeletePressed = {
                        viewModel.submitAction(TaskUiAction.DeleteTaskPressed)
                    },
                    onUpPressed = {
                        viewModel.submitAction(TaskUiAction.NavigateBackPressed)
                    },
                    onDeleteConfirmed = {
                        viewModel.submitAction(TaskUiAction.DeleteTaskConfirmed(it))
                    },
                    onDeleteDismissed = {
                        viewModel.submitAction(TaskUiAction.DeleteTaskDismissed)
                    }
                )
            }
        }
        val context = LocalContext.current
        LaunchedEffect(Unit) {
            viewModel.singleEventFlow.collectLatest {
                when(it) {
                    TaskUiEvent.TaskUpdated -> {
                        showSnackbar(appState, context.getString(R.string.task_updated))
                    }
                    TaskUiEvent.NavigateBack -> {
                        appState.navController.navigateUp()
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

fun NavGraphBuilder.taskScreen(appState: FocusedAppState) {
    composable(
        route = TaskScreenSpec.route,
        arguments = TaskScreenSpec.arguments,
        deepLinks = TaskScreenSpec.deepLinks
    ) {
        TaskScreenSpec.Content(appState = appState, navBackStackEntry = it)
    }
}