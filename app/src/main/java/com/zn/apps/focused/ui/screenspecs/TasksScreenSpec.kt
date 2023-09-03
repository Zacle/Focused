package com.zn.apps.focused.ui.screenspecs

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavBackStackEntry
import com.zn.apps.focused.ui.FocusedAppState
import com.zn.apps.ui_design.icon.FAIcons
import com.zn.apps.ui_design.icon.Icon
import com.zn.apps.ui_design.icon.Icon.DrawableResourceIcon
import com.zn.apps.feature.tasks.R as tasksR

data object TasksScreenSpec: BottomNavScreenSpec {

    override val icon: Icon = DrawableResourceIcon(FAIcons.TaskDestination)

    override val label: Int = tasksR.string.tasks

    override val route: String = "tasks"

    @Composable
    override fun TopBar(appState: FocusedAppState, navBackStackEntry: NavBackStackEntry) {
        TODO("Not yet implemented")
    }

    @Composable
    override fun Content(appState: FocusedAppState, navBackStackEntry: NavBackStackEntry) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(text = "Tasks")
        }
        /*val viewModel: TasksViewModel = hiltViewModel()

        LaunchedEffect(Unit) {
            viewModel.submitAction(TasksUiAction.Load)
        }

        val selectedTagId by viewModel.selectedTag.collectAsStateWithLifecycle()

        viewModel.uiStateFlow.collectAsStateWithLifecycle().value.let { state ->
            CommonScreen(
                state = state,
                tryAgain = {
                    viewModel.submitAction(TasksUiAction.Load)
                }
            ) { tasksUiModel ->
                TasksRoute(
                    tasksUiModel = tasksUiModel,
                    selectedTagId = selectedTagId,
                    snackbarHostState = appState.snackbarHostState,
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
                    TasksUiEvent.TaskNotUpdated -> {
                        showSnackbar(appState, context.getString(R.string.task_not_updated))
                    }
                    TasksUiEvent.TaskNotDeleted -> {
                        showSnackbar(appState, context.getString(R.string.task_not_deleted))
                    }
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
                        // TODO navigate to task using the id
                    }
                    is TasksUiEvent.NavigateToRelatedTasks -> {
                        // TODO navigate to related tasks
                    }
                }
            }
        }*/
    }

    /*private suspend fun showSnackbar(appState: FocusedAppState, message: String) {
        appState.coroutineScope.launch {
            appState.snackbarHostState.showSnackbar(
                message = message,
                withDismissAction = true
            )
        }
    }*/
}