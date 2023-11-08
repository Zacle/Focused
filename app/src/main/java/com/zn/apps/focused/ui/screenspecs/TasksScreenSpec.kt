package com.zn.apps.focused.ui.screenspecs

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.material3.DrawerValue
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
import com.zn.apps.model.datastore.ReminderPreferences
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
        val reminderPreferences by viewModel.reminderPreferences.collectAsStateWithLifecycle(
            initialValue = ReminderPreferences.initPreferences
        )

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
                    defaultTaskReminder = reminderPreferences.taskReminder,
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
                    onStartTaskPressed = { task ->
                        viewModel.submitAction(TasksUiAction.StartRunningTaskPressed(task))
                    },
                    onDrawerPressed = {
                        appState.coroutineScope.launch {
                            appState.drawerState.animateTo(
                                targetValue = DrawerValue.Open,
                                anim = tween(600, easing = FastOutSlowInEasing)
                            )
                        }
                    }
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
                    TasksUiEvent.TaskIsAlreadyRunning -> {
                        showSnackbar(appState, context.getString(R.string.task_already_running))
                    }
                    is TasksUiEvent.NavigateToTimer -> {
                        appState.navigateToTopLevelDestination(TimerScreenSpec.buildRoute(it.taskId))
                    }
                    is TasksUiEvent.NavigateToTask -> {
                        appState.navController.navigate(TaskScreenSpec.buildRoute(it.taskId))
                    }
                    is TasksUiEvent.NavigateToRelatedTasks -> {
                        appState.navController.navigate(DueDateTasksScreenSpec.buildRoute(it.deadlineType))
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