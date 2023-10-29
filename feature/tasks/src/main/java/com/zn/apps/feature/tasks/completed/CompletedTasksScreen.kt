package com.zn.apps.feature.tasks.completed

import android.annotation.SuppressLint
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.zn.apps.feature.tasks.R
import com.zn.apps.model.data.task.RelatedTasksMetaDataResult
import com.zn.apps.model.data.task.Task
import com.zn.apps.model.data.task.TaskResource
import com.zn.apps.ui_design.component.EmptyScreen
import com.zn.apps.ui_design.component.FATopAppBar
import com.zn.apps.ui_design.component.TagChips
import com.zn.apps.ui_design.component.TaskCard
import com.zn.apps.ui_design.icon.FAIcons
import com.zn.apps.ui_design.icon.Icon

@SuppressLint("UnusedCrossfadeTargetStateParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CompletedTasksRoute(
    completedTasksUiModel: CompletedTasksUiModel,
    selectedTagId: String,
    onTaskPressed: (String) -> Unit,
    onSetTaskUnCompleted: (Task) -> Unit,
    onTagPressed: (String) -> Unit,
    onUpPressed: () -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior(rememberTopAppBarState())

    Scaffold(
        topBar = {
            FATopAppBar(
                titleName = stringResource(id = R.string.completed_tasks),
                navigationIcon = Icon.ImageVectorIcon(FAIcons.ArrowBack),
                onNavigationIconClicked = onUpPressed,
                scrollBehavior = scrollBehavior
            ) {
                TagChips(
                    tags = completedTasksUiModel.tags,
                    selectedTagId = selectedTagId,
                    onTagSelected = onTagPressed
                )
            }
        },
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection)
    ) { innerPadding ->

        Crossfade(
            targetState = selectedTagId,
            label = "tasks screen selected id",
            animationSpec = tween(durationMillis = 1_000, easing = LinearOutSlowInEasing),
            modifier = Modifier
                .fillMaxWidth()
        ) {
            CompletedTasksContent(
                completedTasksUiModel = completedTasksUiModel,
                onTaskPressed = onTaskPressed,
                onSetTaskUnCompleted = onSetTaskUnCompleted,
                modifier = Modifier.padding(innerPadding)
            )
        }

    }
}

@Composable
fun CompletedTasksContent(
    completedTasksUiModel: CompletedTasksUiModel,
    onTaskPressed: (String) -> Unit,
    onSetTaskUnCompleted: (Task) -> Unit,
    modifier: Modifier = Modifier
) {
    if (completedTasksUiModel.groupedTasks.isEmpty()) {
        EmptyScreen(
            message = stringResource(id = R.string.no_tasks_completed),
            description = "",
            resId = R.drawable.no_tasks
        )
    } else {
        CompletedTaskList(
            completedTasksMetadata = completedTasksUiModel.groupedTasks,
            onTaskPressed = onTaskPressed,
            onSetTaskUnCompleted = onSetTaskUnCompleted,
            modifier = modifier
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CompletedTaskList(
    completedTasksMetadata: Map<String, RelatedTasksMetaDataResult>,
    onTaskPressed: (String) -> Unit,
    onSetTaskUnCompleted: (Task) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.Center
    ) {
        completedTasksMetadata.onEachIndexed { groupIndex, (header, metadataResult) ->
            item {
                Timeline(
                    groupIndex = groupIndex,
                    elementIndex = HEADER_INDEX,
                    timeLineCircleParameters = TimeLineCircleParametersDefault.circleParameters(
                        backgroundColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.7f),
                        circleColor = MaterialTheme.colorScheme.onPrimary
                    ),
                    lineParameters = LineParametersDefault.lineParameters(
                        lineColor = MaterialTheme.colorScheme.tertiary
                    ),
                    timeLinePadding = TimeLinePaddingDefault.paddingParameters(),
                    isHeader = true,
                    header = {
                        var estimatedTime = ""
                        estimatedTime += metadataResult.elapsedTime.hours.toString() + "h"
                        estimatedTime += metadataResult.elapsedTime.minutes.toString() + "m"
                        TimelineHeader(
                            title = header,
                            subtitle = stringResource(id = R.string.focus) + ": $estimatedTime, " +
                                    stringResource(id = R.string.completed) + ": " +
                                    pluralStringResource(id = R.plurals.task_completed, metadataResult.tasks.size, metadataResult.tasks.size)
                        )
                    },
                    content = {}
                )
            }
            itemsIndexed(metadataResult.tasks, key = { _, item -> item.task.id }) { elementIndex, taskResource: TaskResource ->
                Timeline(
                    groupIndex = groupIndex,
                    elementIndex = elementIndex,
                    timeLineCircleParameters = TimeLineCircleParametersDefault.circleParameters(
                        backgroundColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.7f),
                        circleColor = MaterialTheme.colorScheme.onPrimary
                    ),
                    lineParameters = LineParametersDefault.lineParameters(
                        lineColor = MaterialTheme.colorScheme.tertiary
                    ),
                    timeLinePadding = TimeLinePaddingDefault.paddingParameters(),
                    isHeader = false,
                    header = {},
                    content = {
                        TaskCard(
                            task = taskResource.task,
                            isTaskRunning = false,
                            cardOffset = 0f,
                            navigateToTask = {
                                onTaskPressed(it)
                            },
                            onCompleted = {
                                onSetTaskUnCompleted(it)
                            },
                            onStartTask = {},
                            onExpand = {},
                            onCollapse = {},
                            modifier = Modifier
                                .fillMaxWidth()
                                .size(70.dp)
                                .padding(bottom = 4.dp, end = 16.dp)
                        )
                    }
                )
            }
        }
    }
}