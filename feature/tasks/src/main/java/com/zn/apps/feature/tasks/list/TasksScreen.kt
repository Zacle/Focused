package com.zn.apps.feature.tasks.list

import android.annotation.SuppressLint
import androidx.activity.compose.BackHandler
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.zn.apps.common.getDeadlineTypeValueFromString
import com.zn.apps.common.minutesToMilliseconds
import com.zn.apps.feature.tasks.R
import com.zn.apps.model.data.task.RelatedTasksMetaDataResult
import com.zn.apps.model.data.task.Task
import com.zn.apps.ui_common.InsertTaskBottomSheetContent
import com.zn.apps.ui_common.delegate.TasksUiStateHolder
import com.zn.apps.ui_design.component.DraggableTaskCard
import com.zn.apps.ui_design.component.EmptyScreen
import com.zn.apps.ui_design.component.FAFloatingButton
import com.zn.apps.ui_design.component.FATopAppBar
import com.zn.apps.ui_design.component.TagChips
import com.zn.apps.ui_design.icon.FAIcons
import com.zn.apps.ui_design.icon.Icon.DrawableResourceIcon
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TasksRoute(
    tasksUiModel: TasksUiModel,
    uiStateHolder: TasksUiStateHolder,
    selectedTagId: String,
    snackbarHostState: SnackbarHostState,
    coroutineScope: CoroutineScope,
    onRelatedTasksSelected: (Int) -> Unit,
    onTagPressed: (String) -> Unit,
    onTaskCompleted: (Task) -> Unit,
    upsertTask: (Task) -> Unit,
    onStartTaskPressed: (Task) -> Unit,
) {
    val bottomSheetState = rememberModalBottomSheetState()
    var showModalBottomSheet by remember {
        mutableStateOf(false)
    }

    BackHandler(bottomSheetState.isVisible) {
        coroutineScope.launch {
            bottomSheetState.hide()
        }.invokeOnCompletion {
            if (!bottomSheetState.isVisible) {
                showModalBottomSheet = false
            }
        }
    }

    if (showModalBottomSheet) {
        ModalBottomSheet(
            onDismissRequest = { showModalBottomSheet = false },
            sheetState = bottomSheetState,
            modifier = Modifier
                .navigationBarsPadding()
        ) {
            InsertTaskBottomSheetContent(
                upsertTask = upsertTask,
                projects = uiStateHolder.projects,
                tags = uiStateHolder.tags,
                shouldShowModalSheet = { showModalBottomSheet = it }
            )
        }
    }

    TasksScreen(
        tasksUiModel = tasksUiModel,
        selectedTagId = selectedTagId,
        snackbarHostState = snackbarHostState,
        onTagSelected = onTagPressed,
        onRelatedTasksSelected = onRelatedTasksSelected,
        onTaskCompleted = onTaskCompleted,
        onStartTaskPressed = onStartTaskPressed,
        shouldShowModalSheet = { showModalBottomSheet = it }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedCrossfadeTargetStateParameter")
@Composable
fun TasksScreen(
    tasksUiModel: TasksUiModel,
    selectedTagId: String,
    snackbarHostState: SnackbarHostState,
    onTagSelected: (String) -> Unit,
    onRelatedTasksSelected: (Int) -> Unit,
    onTaskCompleted: (Task) -> Unit,
    onStartTaskPressed: (Task) -> Unit,
    shouldShowModalSheet: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())

    Scaffold(
        containerColor = Color.Transparent,
        contentColor = MaterialTheme.colorScheme.onBackground,
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        topBar = {
            FATopAppBar(
                titleName = stringResource(id = R.string.tasks),
                onNavigationIconClicked = {},
                navigationIcon = DrawableResourceIcon(FAIcons.menu),
                scrollBehavior = scrollBehavior
            ) {
                TagChips(
                    tags = tasksUiModel.tags,
                    selectedTagId = selectedTagId,
                    onTagSelected = onTagSelected
                )
            }
        },
        floatingActionButton = {
            FAFloatingButton(
                onClick = { shouldShowModalSheet(true) },
                painter = painterResource(id = R.drawable.add_task),
                description = stringResource(id = R.string.click_to_add_task)
            )
        },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        modifier = modifier
            .nestedScroll(scrollBehavior.nestedScrollConnection)
    ) { innerPadding ->

        Crossfade(
            targetState = selectedTagId,
            label = "tasks screen selected id",
            modifier = Modifier
                .fillMaxWidth()
        ) {
            TasksContent(
                tasksUiModel = tasksUiModel,
                innerPadding = innerPadding,
                onRelatedTasksSelected = onRelatedTasksSelected,
                onStartTaskPressed = onStartTaskPressed,
                onTaskCompleted = onTaskCompleted
            )
        }
    }
}

@Composable
fun TasksContent(
    tasksUiModel: TasksUiModel,
    innerPadding: PaddingValues,
    onRelatedTasksSelected: (Int) -> Unit,
    onStartTaskPressed: (Task) -> Unit,
    onTaskCompleted: (Task) -> Unit
) {
    if (tasksUiModel.groupedTasks.isEmpty()) {
        EmptyScreen(
            message = stringResource(id = R.string.no_tasks),
            description = stringResource(id = R.string.click_to_add_task),
            resId = R.drawable.no_tasks
        )
    } else {
        TaskList(
            tasksMetaData = tasksUiModel.groupedTasks,
            onRelatedTasksSelected = onRelatedTasksSelected,
            setTaskCompleted = onTaskCompleted,
            startRunningTask = onStartTaskPressed,
            modifier = Modifier.padding(innerPadding)
        )
    }
}

@Composable
fun TaskList(
    tasksMetaData: Map<String, RelatedTasksMetaDataResult>,
    onRelatedTasksSelected: (Int) -> Unit,
    setTaskCompleted: (Task) -> Unit,
    startRunningTask: (Task) -> Unit,
    modifier: Modifier
) {
    LazyColumn(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.Center
    ) {
        tasksMetaData.forEach {(dueDate, tasksMetaDataResult) ->

            item {
                TaskListSection(
                    sectionTitle = stringResource(id = getDeadlineTypeValueFromString(dueDate)),
                    tasksMetaDataResult = tasksMetaDataResult,
                    onRelatedTasksSelected = {
                        onRelatedTasksSelected(getDeadlineTypeValueFromString(dueDate))
                    }
                )
            }

            items(tasksMetaDataResult.tasks.take(3), key = { it.task.id }) { taskResource ->
                TaskSection(
                    task = taskResource.task,
                    setTaskCompleted = setTaskCompleted,
                    startRunningTask = startRunningTask
                )
            }
        }
    }
}

@Composable
fun TaskSection(
    task: Task,
    setTaskCompleted: (Task) -> Unit,
    startRunningTask: (Task) -> Unit,
) {
    val viewModel: TasksViewModel = hiltViewModel()
    val uiStateHolder by viewModel.tasksUiStateHolder.collectAsStateWithLifecycle()

    DraggableTaskCard(
        task = task,
        isTaskRunning = false,
        showDeleteDialog = uiStateHolder.showDeleteTaskDialog,
        showDueDateDialog = uiStateHolder.showDueDateDialog,
        showPomodoroDialog = uiStateHolder.showPomodoroDialog,
        isTaskRevealed = uiStateHolder.taskPressed?.id == task.id,
        navigateToTask = {
            viewModel.submitAction(TasksUiAction.NavigateToTask(task.id))
        },
        onCompleted = setTaskCompleted,
        onStartTask = { startRunningTask(task) },
        onDeleteTaskPressed = {
            viewModel.submitAction(TasksUiAction.DeleteTaskPressed(task))
        },
        onDeleteTaskDismissed = { viewModel.submitAction(TasksUiAction.DeleteTaskDismissed) },
        onDeleteTaskConfirmed = { viewModel.submitAction(TasksUiAction.DeleteTaskConfirmed) },
        onUpdateDueDatePressed = {
            viewModel.submitAction(TasksUiAction.UpdateDueDatePressed(task))
        },
        onUpdateDueDateDismissed = { viewModel.submitAction(TasksUiAction.UpdateDueDateDismissed) },
        onUpdateDueDateConfirmed = {
            viewModel.submitAction(TasksUiAction.UpdatedDueDateConfirmed(it))
        },
        onUpdatePomodoroPressed = {
            viewModel.submitAction(TasksUiAction.UpdatePomodoroPressed(task))
        },
        onUpdatePomodoroDismissed = { viewModel.submitAction(TasksUiAction.UpdatePomodoroDismissed) },
        onUpdatePomodoroConfirmed = {
            viewModel.submitAction(
                TasksUiAction.UpdatePomodoroConfirmed(
                    pomodoro = task.pomodoro.copy(
                        pomodoroNumber = it.pomodoroNumber,
                        pomodoroLength = it.pomodoroLength.minutesToMilliseconds()
                    )
                )
            )
        },
        onExpand = { viewModel.expand(task) },
        onCollapse = { viewModel.collapse() }
    )
}

@Composable
fun TaskListSection(
    sectionTitle: String,
    tasksMetaDataResult: RelatedTasksMetaDataResult,
    modifier: Modifier = Modifier,
    onRelatedTasksSelected: (() -> Unit)? = null
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(modifier = Modifier.weight(1.0f)) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = sectionTitle,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.padding(end = 8.dp)
                )
                val color = MaterialTheme.colorScheme.onSurface
                Canvas(modifier = Modifier
                    .size(4.dp)
                ) {
                    drawCircle(color = color)
                }
                var estimatedTime = ""
                if (tasksMetaDataResult.estimatedTime.hours > 0) estimatedTime += tasksMetaDataResult.estimatedTime.hours.toString() + "h"
                if (tasksMetaDataResult.estimatedTime.minutes > 0) estimatedTime += tasksMetaDataResult.estimatedTime.minutes.toString() + "m"
                Text(
                    text = estimatedTime,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.tertiary,
                    modifier = Modifier.padding(start = 8.dp)
                )
            }
        }

        if (onRelatedTasksSelected != null) {
            TextButton(onClick = { onRelatedTasksSelected() }) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = stringResource(id = R.string.see_all),
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.secondary
                    )
                    Icon(
                        imageVector = FAIcons.ArrowForward,
                        contentDescription = stringResource(id = R.string.see_all),
                        tint = MaterialTheme.colorScheme.secondary,
                        modifier = Modifier.size(12.dp)
                    )
                }
            }
        }
    }
}