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
import com.zn.apps.focused.R
import com.zn.apps.focused.ui.FocusedAppState
import com.zn.apps.ui_common.related_tasks.PROJECT_ID_ARGUMENT
import com.zn.apps.ui_common.related_tasks.ProjectWithTasksViewModel
import com.zn.apps.ui_common.related_tasks.RelatedTasksRoute
import com.zn.apps.ui_common.related_tasks.RelatedTasksUiAction
import com.zn.apps.ui_common.related_tasks.RelatedTasksUiEvent
import com.zn.apps.ui_common.state.CommonScreen
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

data object ProjectWithTasksScreenSpec: ScreenSpec {
    override val route: String = "projectTasks/{${PROJECT_ID_ARGUMENT}}"

    override val arguments: List<NamedNavArgument>
        get() = listOf(
            navArgument(PROJECT_ID_ARGUMENT) { type = NavType.StringType }
        )

    fun buildRoute(projectId: String) = "projectTasks/$projectId"

    @Composable
    override fun Content(appState: FocusedAppState, navBackStackEntry: NavBackStackEntry) {
        val viewModel: ProjectWithTasksViewModel = hiltViewModel()

        LaunchedEffect(Unit) {
            viewModel.submitAction(RelatedTasksUiAction.Load)
        }

        val groupingType by viewModel.groupingType.collectAsStateWithLifecycle()
        val uiStateHolder by viewModel.tasksUiStateHolder.collectAsStateWithLifecycle()

        viewModel.uiStateFlow.collectAsStateWithLifecycle().value.let { state ->
            CommonScreen(state = state) { relatedTasksUiModel ->
                RelatedTasksRoute(
                    relatedTasksUiModel = relatedTasksUiModel,
                    uiStateHolder = uiStateHolder,
                    selectedGroupingType = groupingType,
                    isProjectTasks = true,
                    setGroupingType = {
                        viewModel.submitAction(RelatedTasksUiAction.SetGrouping(it))
                    },
                    upsertTask = {
                        viewModel.upsertTask(it)
                    },
                    upPressed = { appState.navController.navigateUp() },
                    coroutineScope = appState.coroutineScope
                )
            }
        }

        val context = LocalContext.current

        LaunchedEffect(Unit) {
            viewModel.singleEventFlow.collectLatest {
                when(it) {
                    RelatedTasksUiEvent.TaskDeleted -> {
                        showSnackbar(appState, context.getString(R.string.task_deleted))
                    }
                    RelatedTasksUiEvent.DueDateUpdated -> {
                        showSnackbar(appState, context.getString(R.string.due_date_updated))
                    }
                    RelatedTasksUiEvent.PomodoroUpdated -> {
                        showSnackbar(appState, context.getString(R.string.pomodoro_updated))
                    }
                    RelatedTasksUiEvent.TaskIsAlreadyRunning -> {
                        showSnackbar(
                            appState,
                            context.getString(R.string.task_already_running)
                        )
                    }
                    is RelatedTasksUiEvent.NavigateToTask -> {
                        appState.navController.navigate(TaskScreenSpec.buildRoute(it.taskId))
                    }
                    is RelatedTasksUiEvent.NavigateToTimer -> {
                        appState.navController.navigate(TimerScreenSpec.buildRoute(it.taskId))
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

fun NavGraphBuilder.projectWithTasksScreen(appState: FocusedAppState) {
    composable(
        route = ProjectWithTasksScreenSpec.route,
        arguments = ProjectWithTasksScreenSpec.arguments,
        deepLinks = ProjectWithTasksScreenSpec.deepLinks
    ) {
        ProjectWithTasksScreenSpec.Content(appState = appState, navBackStackEntry = it)
    }
}