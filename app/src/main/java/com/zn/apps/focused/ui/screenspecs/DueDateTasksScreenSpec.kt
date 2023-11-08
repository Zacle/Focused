package com.zn.apps.focused.ui.screenspecs

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
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
import com.zn.apps.model.datastore.ReminderPreferences
import com.zn.apps.ui_common.related_tasks.DEADLINE_TYPE_ARGUMENT
import com.zn.apps.ui_common.related_tasks.DueDateTasksViewModel
import com.zn.apps.ui_common.related_tasks.RelatedTasksRoute
import com.zn.apps.ui_common.related_tasks.RelatedTasksUiAction
import com.zn.apps.ui_common.related_tasks.RelatedTasksUiEvent
import com.zn.apps.ui_common.state.CommonScreen
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

data object DueDateTasksScreenSpec: ScreenSpec {
    override val route: String = "relatedTasks/{${DEADLINE_TYPE_ARGUMENT}}"

    override val arguments: List<NamedNavArgument>
        get() = listOf(
            navArgument(DEADLINE_TYPE_ARGUMENT) { type = NavType.IntType }
        )

    fun buildRoute(deadlineTypeArg: Int) = "relatedTasks/$deadlineTypeArg"

    @Composable
    override fun Content(appState: FocusedAppState, navBackStackEntry: NavBackStackEntry) {
        val viewModel: DueDateTasksViewModel = hiltViewModel()

        LaunchedEffect(Unit) {
            viewModel.submitAction(RelatedTasksUiAction.Load)
        }

        val groupingType by viewModel.groupingType.collectAsStateWithLifecycle()
        val uiStateHolder by viewModel.tasksUiStateHolder.collectAsStateWithLifecycle()
        val reminderPreferences by viewModel.reminderPreferences.collectAsStateWithLifecycle(
            initialValue = ReminderPreferences.initPreferences
        )

        viewModel.uiStateFlow.collectAsStateWithLifecycle().value.let { state ->
            CommonScreen(state = state) { relatedTasksUiModel ->
                RelatedTasksRoute(
                    relatedTasksUiModel = relatedTasksUiModel.copy(
                        screenTitle = stringResource(id = viewModel.deadlineType.value)
                    ),
                    defaultTaskReminder = reminderPreferences.taskReminder,
                    uiStateHolder = uiStateHolder,
                    selectedGroupingType = groupingType,
                    isProjectTasks = false,
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
                        showSnackbar(
                            appState,
                            context.getString(R.string.task_deleted)
                        )
                    }
                    RelatedTasksUiEvent.DueDateUpdated -> {
                        showSnackbar(
                            appState,
                            context.getString(R.string.due_date_updated)
                        )
                    }
                    RelatedTasksUiEvent.PomodoroUpdated -> {
                        showSnackbar(
                            appState,
                            context.getString(R.string.pomodoro_updated)
                        )
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

fun NavGraphBuilder.dueDateTasksScreen(appState: FocusedAppState) {
    composable(
        route = DueDateTasksScreenSpec.route,
        arguments = DueDateTasksScreenSpec.arguments,
        deepLinks = DueDateTasksScreenSpec.deepLinks
    ) {
        DueDateTasksScreenSpec.Content(appState = appState, navBackStackEntry = it)
    }
}