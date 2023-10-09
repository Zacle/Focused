package com.zn.apps.feature.tasks.list

import android.annotation.SuppressLint
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.zn.apps.common.getDeadlineTypeIndexFromString
import com.zn.apps.common.getDeadlineTypeValueFromString
import com.zn.apps.common.minutesToMilliseconds
import com.zn.apps.feature.tasks.R
import com.zn.apps.model.data.task.RelatedTasksMetaDataResult
import com.zn.apps.model.data.task.Task
import com.zn.apps.ui_common.TaskListSection
import com.zn.apps.ui_common.TaskModalSheetSection
import com.zn.apps.ui_common.delegate.TasksUiStateHolder
import com.zn.apps.ui_design.component.DraggableTaskCard
import com.zn.apps.ui_design.component.EmptyScreen
import com.zn.apps.ui_design.component.FAFloatingButton
import com.zn.apps.ui_design.component.FATopAppBar
import com.zn.apps.ui_design.component.TagChips
import com.zn.apps.ui_design.icon.FAIcons
import com.zn.apps.ui_design.icon.Icon.DrawableResourceIcon
import kotlinx.coroutines.CoroutineScope

@Composable
fun TasksRoute(
    tasksUiModel: TasksUiModel,
    uiStateHolder: TasksUiStateHolder,
    selectedTagId: String,
    coroutineScope: CoroutineScope,
    onRelatedTasksSelected: (Int) -> Unit,
    onTagPressed: (String) -> Unit,
    onTaskCompleted: (Task) -> Unit,
    upsertTask: (Task) -> Unit,
    onStartTaskPressed: (Task) -> Unit,
    onDrawerPressed: () -> Unit
) {
    var showModalBottomSheet by remember {
        mutableStateOf(false)
    }

    TaskModalSheetSection(
        coroutineScope = coroutineScope,
        upsertTask = upsertTask,
        uiStateHolder = uiStateHolder,
        showModalBottomSheet = showModalBottomSheet,
        shouldShowModalSheet = { showModalBottomSheet = it }
    )

    TasksScreen(
        tasksUiModel = tasksUiModel,
        selectedTagId = selectedTagId,
        onTagSelected = onTagPressed,
        onRelatedTasksSelected = onRelatedTasksSelected,
        onTaskCompleted = onTaskCompleted,
        onStartTaskPressed = onStartTaskPressed,
        shouldShowModalSheet = { showModalBottomSheet = it },
        onDrawerPressed = onDrawerPressed
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedCrossfadeTargetStateParameter")
@Composable
fun TasksScreen(
    tasksUiModel: TasksUiModel,
    selectedTagId: String,
    onTagSelected: (String) -> Unit,
    onRelatedTasksSelected: (Int) -> Unit,
    onTaskCompleted: (Task) -> Unit,
    onStartTaskPressed: (Task) -> Unit,
    shouldShowModalSheet: (Boolean) -> Unit,
    onDrawerPressed: () -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior(rememberTopAppBarState())

    Scaffold(
        containerColor = Color.Transparent,
        contentColor = MaterialTheme.colorScheme.onBackground,
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        topBar = {
            FATopAppBar(
                titleName = stringResource(id = R.string.tasks),
                onNavigationIconClicked = onDrawerPressed,
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
                        onRelatedTasksSelected(getDeadlineTypeIndexFromString(dueDate))
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
            viewModel.submitAction(TasksUiAction.DeleteTaskPressed)
        },
        onDeleteTaskDismissed = { viewModel.submitAction(TasksUiAction.DeleteTaskDismissed) },
        onDeleteTaskConfirmed = { viewModel.submitAction(TasksUiAction.DeleteTaskConfirmed) },
        onUpdateDueDatePressed = {
            viewModel.submitAction(TasksUiAction.UpdateDueDatePressed)
        },
        onUpdateDueDateDismissed = { viewModel.submitAction(TasksUiAction.UpdateDueDateDismissed) },
        onUpdateDueDateConfirmed = {
            viewModel.submitAction(TasksUiAction.UpdatedDueDateConfirmed(it))
        },
        onUpdatePomodoroPressed = {
            viewModel.submitAction(TasksUiAction.UpdatePomodoroPressed)
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