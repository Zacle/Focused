package com.zn.apps.focused.ui.screenspecs

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavBackStackEntry
import com.zn.apps.feature.tasks.list.TasksRoute
import com.zn.apps.feature.tasks.list.TasksUiAction
import com.zn.apps.feature.tasks.list.TasksUiEvent
import com.zn.apps.feature.tasks.list.TasksViewModel
import com.zn.apps.focused.R
import com.zn.apps.focused.ui.FocusedAppState
import com.zn.apps.ui_common.state.CommonScreen
import com.zn.apps.ui_design.icon.FAIcons
import com.zn.apps.ui_design.icon.Icon
import com.zn.apps.ui_design.icon.Icon.DrawableResourceIcon
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import com.zn.apps.feature.tasks.R as tasksR

data object TasksScreenSpec: BottomNavScreenSpec {

    override val icon: Icon = DrawableResourceIcon(FAIcons.TaskDestination)

    override val label: Int = tasksR.string.tasks

    override val route: String = "tasks"

    @Composable
    override fun Content(appState: FocusedAppState, navBackStackEntry: NavBackStackEntry) {
        val viewModel: TasksViewModel = hiltViewModel()

        LaunchedEffect(Unit) {
            viewModel.submitAction(TasksUiAction.Load)
        }

        val selectedTagId by viewModel.selectedTag.collectAsStateWithLifecycle()
        val uiStateHolder by viewModel.tasksUiStateHolder.collectAsStateWithLifecycle()

        viewModel.uiStateFlow.collectAsStateWithLifecycle().value.let { state ->
            CommonScreen(
                state = state,
                tryAgain = {
                    viewModel.submitAction(TasksUiAction.Load)
                }
            ) { tasksUiModel ->
                TasksRoute(
                    tasksUiModel = tasksUiModel,
                    uiStateHolder = uiStateHolder,
                    selectedTagId = selectedTagId,
                    coroutineScope = appState.coroutineScope,
                    onRelatedTasksSelected = { deadlineType ->
                        viewModel.submitAction(TasksUiAction.NavigateToRelatedTasks(deadlineType))
                    },
                    onTagPressed = { tagId ->
                        viewModel.submitAction(TasksUiAction.TagPressed(tagId))
                    },
                    onTaskCompleted = { task ->
                        viewModel.submitAction(TasksUiAction.TaskCompleted(task))
                    },
                    upsertTask = { task ->
                        viewModel.upsertTask(task)
                    },
                    onStartTaskPressed = {}
                )
            }
        }

        val context = LocalContext.current

        LaunchedEffect(Unit) {
            viewModel.singleEventFlow.collectLatest {
                when(it) {
                    TasksUiEvent.TaskDeleted -> {
                        showSnackbar(appState, context.getString(R.string.task_deleted))
                    }
                    TasksUiEvent.DueDateUpdated -> {
                        showSnackbar(appState, context.getString(R.string.due_date_updated))
                    }
                    TasksUiEvent.PomodoroUpdated -> {
                        showSnackbar(appState, context.getString(R.string.pomodoro_updated))
                    }
                    is TasksUiEvent.NavigateToTask -> {
                        appState.navController.navigate(TaskScreenSpec.buildRoute(it.taskId))
                    }
                    is TasksUiEvent.NavigateToRelatedTasks -> {
                        // TODO navigate to related tasks
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