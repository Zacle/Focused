package com.zn.apps.ui_common.related_tasks

import android.annotation.SuppressLint
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.zn.apps.common.MetadataResult
import com.zn.apps.common.MetadataType
import com.zn.apps.common.minutesToMilliseconds
import com.zn.apps.filter.Grouping
import com.zn.apps.model.data.pomodoro.PomodoroState
import com.zn.apps.model.data.task.Task
import com.zn.apps.ui_common.TaskListSection
import com.zn.apps.ui_common.TaskModalSheetSection
import com.zn.apps.ui_common.delegate.TasksUiStateHolder
import com.zn.apps.ui_design.R
import com.zn.apps.ui_design.component.CompletionTime
import com.zn.apps.ui_design.component.DraggableTaskCard
import com.zn.apps.ui_design.component.EmptyScreen
import com.zn.apps.ui_design.component.FAFloatingButton
import com.zn.apps.ui_design.component.FATopAppBar
import com.zn.apps.ui_design.component.FATopAppBarDefaults
import com.zn.apps.ui_design.component.IconMetadata
import com.zn.apps.ui_design.component.PercentageMetadata
import com.zn.apps.ui_design.component.SelectGroupingType
import com.zn.apps.ui_design.component.ThemePreviews
import com.zn.apps.ui_design.icon.FAIcons
import com.zn.apps.ui_design.icon.Icon
import kotlinx.coroutines.CoroutineScope

@Composable
fun RelatedTasksRoute(
    relatedTasksUiModel: RelatedTasksUiModel,
    defaultTaskReminder: Int,
    uiStateHolder: TasksUiStateHolder,
    selectedGroupingType: Grouping,
    isProjectTasks: Boolean,
    setGroupingType: (Grouping) -> Unit,
    upsertTask: (Task) -> Unit,
    upPressed: () -> Unit,
    coroutineScope: CoroutineScope
) {
    var showModalBottomSheet by remember {
        mutableStateOf(false)
    }
    var showGroupingTypeDialog by remember {
        mutableStateOf(false)
    }

    if (showGroupingTypeDialog) {
        Dialog(
            onDismissRequest = { showGroupingTypeDialog = false },
        ) {
            SelectGroupingType(
                currentGrouping = selectedGroupingType,
                onGroupingSelected = {
                    showGroupingTypeDialog = false
                    setGroupingType(it)
                },
                onCancel = {
                    showGroupingTypeDialog = false
                }
            )
        }
    }

    TaskModalSheetSection(
        coroutineScope = coroutineScope,
        upsertTask = upsertTask,
        shouldShowModalSheet = { showModalBottomSheet = it },
        uiStateHolder = uiStateHolder,
        showModalBottomSheet = showModalBottomSheet,
        defaultTaskReminder = defaultTaskReminder
    )

    RelatedTasksScreen(
        relatedTasksUiModel = relatedTasksUiModel,
        selectedGroupingType = selectedGroupingType,
        onGroupingPressed = { showGroupingTypeDialog = true },
        isProjectTasks = isProjectTasks,
        upPressed = upPressed,
        shouldShowModalSheet = { showModalBottomSheet = it }
    )
}

@SuppressLint("UnusedCrossfadeTargetStateParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RelatedTasksScreen(
    relatedTasksUiModel: RelatedTasksUiModel,
    selectedGroupingType: Grouping,
    onGroupingPressed: () -> Unit,
    isProjectTasks: Boolean,
    upPressed: () -> Unit,
    shouldShowModalSheet: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {

    Scaffold(
        containerColor = Color.Transparent,
        contentColor = MaterialTheme.colorScheme.onBackground,
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        topBar = {
            FATopAppBar(
                titleName = relatedTasksUiModel.screenTitle,
                navigationIcon = Icon.ImageVectorIcon(FAIcons.ArrowBack),
                onNavigationIconClicked = { upPressed() },
                actions = {
                    IconButton(onClick = onGroupingPressed) {
                        Icon(
                            painter = painterResource(id = FAIcons.filter),
                            contentDescription = stringResource(id = R.string.group_tasks),
                            tint = FATopAppBarDefaults.appBarContentColor(),
                            modifier = Modifier.size(24.dp)
                        )
                    }
                },
            ) {}
        },
        floatingActionButton = {
            FAFloatingButton(
                onClick = { shouldShowModalSheet(true) },
                painter = painterResource(id = FAIcons.add_time),
                description = stringResource(id = R.string.click_to_add_task),
                modifier = Modifier.navigationBarsPadding()
            )
        },
        modifier = modifier
    ) { innerPadding ->
        Crossfade(
            targetState = selectedGroupingType,
            label = "related tasks scree grouping",
            modifier = Modifier
                .fillMaxWidth()
        ) {
            RelatedTasksContent(
                relatedTasksUiModel = relatedTasksUiModel,
                isProjectTasks = isProjectTasks,
                modifier = Modifier.padding(innerPadding)
            )
        }
    }
}

@Composable
fun RelatedTasksContent(
    relatedTasksUiModel: RelatedTasksUiModel,
    isProjectTasks: Boolean,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.Center
    ) {
        item { 
            if (relatedTasksUiModel.metadata.metadataType == MetadataType.SHOW) {
                RelatedTasksShowMetadata(metadataResult = relatedTasksUiModel.metadata)
            } else {
                RelatedTasksHideMetadata(metadataResult = relatedTasksUiModel.metadata)
            }
        }

        if (relatedTasksUiModel.relatedTasksGrouped.isEmpty()) {
            item {
                EmptyScreen(
                    message = stringResource(id = R.string.no_tasks),
                    description = stringResource(id = R.string.click_to_add_task),
                    resId = R.drawable.no_tasks
                )
            }
        } else {
            relatedTasksUiModel.relatedTasksGrouped.forEach { (groupKey, relatedTasksMetaDataResult) ->

                item {
                    TaskListSection(
                        sectionTitle = groupKey,
                        tasksMetaDataResult = relatedTasksMetaDataResult,
                        modifier = Modifier
                            .padding(vertical = 8.dp)
                    )
                }

                items(relatedTasksMetaDataResult.tasks, key = { it.task.id }) { taskResource ->
                    if (isProjectTasks) {
                        ProjectTaskSection(
                            task = taskResource.task
                        )
                    } else {
                        RelatedTaskSection(
                            task = taskResource.task
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ProjectTaskSection(
    task: Task
) {
    val viewModel: ProjectWithTasksViewModel = hiltViewModel()
    val uiStateHolder by viewModel.tasksUiStateHolder.collectAsStateWithLifecycle()
    val pomodoroState by viewModel.pomodoroState.collectAsStateWithLifecycle(initialValue = PomodoroState.initialState)

    DraggableTaskCard(
        task = task,
        isTaskRunning = pomodoroState.taskIdRunning == task.id,
        showDeleteDialog = uiStateHolder.showDeleteTaskDialog,
        showDueDateDialog = uiStateHolder.showDueDateDialog,
        showPomodoroDialog = uiStateHolder.showPomodoroDialog,
        isTaskRevealed = uiStateHolder.taskPressed?.id == task.id,
        navigateToTask = {
            viewModel.submitAction(RelatedTasksUiAction.NavigateToTask(task.id))
        },
        onCompleted = { viewModel.setTaskCompleted(task) },
        onStartTask = { viewModel.submitAction(RelatedTasksUiAction.StartRunningTaskPressed(task)) },
        onDeleteTaskPressed = {
            viewModel.submitAction(RelatedTasksUiAction.DeleteTaskPressed)
        },
        onDeleteTaskDismissed = { viewModel.submitAction(RelatedTasksUiAction.DeleteTaskDismissed) },
        onDeleteTaskConfirmed = { viewModel.submitAction(RelatedTasksUiAction.DeleteTaskConfirmed) },
        onUpdateDueDatePressed = {
            viewModel.submitAction(RelatedTasksUiAction.UpdateDueDatePressed)
        },
        onUpdateDueDateDismissed = { viewModel.submitAction(RelatedTasksUiAction.UpdateDueDateDismissed) },
        onUpdateDueDateConfirmed = { dueDate, remindBefore, isReminderSet ->
            viewModel.submitAction(
                RelatedTasksUiAction.UpdateDueDateConfirmed(dueDate, remindBefore, isReminderSet)
            )
        },
        onUpdatePomodoroPressed = {
            viewModel.submitAction(RelatedTasksUiAction.UpdatePomodoroPressed)
        },
        onUpdatePomodoroDismissed = { viewModel.submitAction(RelatedTasksUiAction.UpdatePomodoroDismissed) },
        onUpdatePomodoroConfirmed = {
            viewModel.submitAction(
                RelatedTasksUiAction.UpdatePomodoroConfirmed(
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
fun RelatedTaskSection(
    task: Task
) {
    val viewModel: DueDateTasksViewModel = hiltViewModel()
    val uiStateHolder by viewModel.tasksUiStateHolder.collectAsStateWithLifecycle()
    val pomodoroState by viewModel.pomodoroState.collectAsStateWithLifecycle(initialValue = PomodoroState.initialState)

    DraggableTaskCard(
        task = task,
        isTaskRunning = pomodoroState.taskIdRunning == task.id,
        showDeleteDialog = uiStateHolder.showDeleteTaskDialog,
        showDueDateDialog = uiStateHolder.showDueDateDialog,
        showPomodoroDialog = uiStateHolder.showPomodoroDialog,
        isTaskRevealed = uiStateHolder.taskPressed?.id == task.id,
        navigateToTask = {
            viewModel.submitAction(RelatedTasksUiAction.NavigateToTask(task.id))
        },
        onCompleted = { viewModel.setTaskCompleted(task) },
        onStartTask = { viewModel.submitAction(RelatedTasksUiAction.StartRunningTaskPressed(task)) },
        onDeleteTaskPressed = {
            viewModel.submitAction(RelatedTasksUiAction.DeleteTaskPressed)
        },
        onDeleteTaskDismissed = { viewModel.submitAction(RelatedTasksUiAction.DeleteTaskDismissed) },
        onDeleteTaskConfirmed = { viewModel.submitAction(RelatedTasksUiAction.DeleteTaskConfirmed) },
        onUpdateDueDatePressed = {
            viewModel.submitAction(RelatedTasksUiAction.UpdateDueDatePressed)
        },
        onUpdateDueDateDismissed = { viewModel.submitAction(RelatedTasksUiAction.UpdateDueDateDismissed) },
        onUpdateDueDateConfirmed = { dueDate, remindBefore, isReminderSet ->
            viewModel.submitAction(
                RelatedTasksUiAction.UpdateDueDateConfirmed(dueDate, remindBefore, isReminderSet)
            )
        },
        onUpdatePomodoroPressed = {
            viewModel.submitAction(RelatedTasksUiAction.UpdatePomodoroPressed)
        },
        onUpdatePomodoroDismissed = { viewModel.submitAction(RelatedTasksUiAction.UpdatePomodoroDismissed) },
        onUpdatePomodoroConfirmed = {
            viewModel.submitAction(
                RelatedTasksUiAction.UpdatePomodoroConfirmed(
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
fun RelatedTasksShowMetadata(
    metadataResult: MetadataResult,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            /**
             * Estimated remaining time
             */
            IconMetadata(
                text = stringResource(id = R.string.estimated_task_time),
                icon = Icon.DrawableResourceIcon(FAIcons.TimerDestination),
                modifier = Modifier.weight(1f)
            ) {
                CompletionTime(
                    hour = metadataResult.estimatedTaskTime.hours,
                    minute = metadataResult.estimatedTaskTime.minutes
                )
            }
            /**
             * Elapsed time
             */
            val percentage =
                if (metadataResult.totalTime > 0)
                    (metadataResult.elapsedTime / metadataResult.totalTime.toDouble()) * 100
                else
                    0
            PercentageMetadata(
                text = stringResource(id = R.string.elapsed_time),
                percentage = percentage.toInt(),
                modifier = Modifier.weight(1f)
            ) {
                CompletionTime(
                    hour = metadataResult.elapsedTaskTime.hours,
                    minute = metadataResult.elapsedTaskTime.minutes
                )
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            /**
             * Tasks to be completed
             */
            IconMetadata(
                text = stringResource(id = R.string.tasks_to_be_completed),
                icon = Icon.DrawableResourceIcon(FAIcons.task),
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = "${metadataResult.tasksToBeCompleted}",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            /**
             * Tasks completed
             */
            val percentage =
                if (metadataResult.totalTasks > 0)
                    (metadataResult.tasksCompleted / metadataResult.totalTasks.toDouble()) * 100
                else
                    0
            PercentageMetadata(
                text = stringResource(id = R.string.completed_tasks),
                percentage = percentage.toInt(),
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = "${metadataResult.tasksCompleted}",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}

@ThemePreviews
@Composable
fun RelatedTasksHideMetadata(
    modifier: Modifier = Modifier,
    metadataResult: MetadataResult = MetadataResult.initialMetadataResult
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp),
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            /**
             * Estimated remaining time
             */
            IconMetadata(
                text = stringResource(id = R.string.estimated_task_time),
                icon = Icon.DrawableResourceIcon(FAIcons.TimerDestination),
                modifier = Modifier
                    .size(hideMetadataHeight)
                    .weight(1f)
            ) {
                CompletionTime(
                    hour = metadataResult.estimatedTaskTime.hours,
                    minute = metadataResult.estimatedTaskTime.minutes
                )
            }
            /**
             * Tasks to be completed
             */
            IconMetadata(
                text = stringResource(id = R.string.tasks_to_be_completed),
                icon = Icon.DrawableResourceIcon(FAIcons.task),
                modifier = Modifier
                    .size(hideMetadataHeight)
                    .weight(1f)
            ) {
                Text(
                    text = "${metadataResult.tasksToBeCompleted}",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}

private val hideMetadataHeight = 120.dp
