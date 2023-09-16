package com.zn.apps.feature.projects.list

import android.annotation.SuppressLint
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.zn.apps.feature.projects.R
import com.zn.apps.model.data.project.Project
import com.zn.apps.model.data.project.ProjectResource
import com.zn.apps.ui_design.component.ACTION_SIZE
import com.zn.apps.ui_design.component.DisplayTime
import com.zn.apps.ui_design.component.SingleAction
import com.zn.apps.ui_design.component.ThemePreviews
import com.zn.apps.ui_design.component.dialog.SimpleConfirmationDialog
import com.zn.apps.ui_design.icon.FAIcons
import com.zn.apps.ui_design.icon.FormIcon
import com.zn.apps.ui_design.icon.Icon
import com.zn.apps.ui_design.theme.FocusedAppTheme
import java.time.OffsetDateTime
import kotlin.math.roundToInt

const val ANIMATION_DURATION = 600
const val DRAG_AMOUNT = 6

@Composable
fun DraggableProjectListCard(
    projectResource: ProjectResource,
    isProjectRevealed: Boolean,
    showCompleteProjectDialog: Boolean,
    showDeleteProjectDialog: Boolean,
    onCompletePressed: () -> Unit,
    onCompleteDismissed: () -> Unit,
    onCompleteConfirmed: () -> Unit,
    onDeletePressed: () -> Unit,
    onDeleteDismissed: () -> Unit,
    onDeleteConfirmed: () -> Unit,
    navigateToProject: (String) -> Unit,
    navigateToEditProject: (String) -> Unit,
    onExpand: () -> Unit,
    onCollapse: () -> Unit,
    modifier: Modifier = Modifier
) {

    var cardOffset by remember {
        mutableFloatStateOf(0f)
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
    ) {
        ProjectActionsRow(
            completed = projectResource.project.completed,
            onDeletePressed = onDeletePressed,
            onCompletedPressed = onCompletePressed,
            onNavigateToProjectPressed = {
                onCollapse()
                navigateToEditProject(projectResource.project.id)
            },
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .onGloballyPositioned {
                    cardOffset = it.size.width.toFloat()
                }
        )
        ProjectListCard(
            projectResource = projectResource,
            navigateToProject = navigateToProject,
            onExpand = onExpand,
            onCollapse = onCollapse,
            isRevealed = isProjectRevealed,
            cardOffset = cardOffset
        )
    }
    if (isProjectRevealed) {
        if (showDeleteProjectDialog) {
            SimpleConfirmationDialog(
                title = R.string.delete_project,
                text = R.string.delete_project_text,
                confirmationAction = onDeleteConfirmed,
                cancelAction = onDeleteDismissed
            )
        }
        val completed = projectResource.project.completed
        if (showCompleteProjectDialog) {
            SimpleConfirmationDialog(
                title =
                if (completed)
                    R.string.incomplete_project
                else
                    R.string.complete_project,
                text =
                if (completed)
                    R.string.incomplete_project_text
                else
                    R.string.complete_project_text,
                confirmationAction = onCompleteConfirmed,
                cancelAction = onCompleteDismissed
            )
        }
    }
}

@Composable
fun ProjectActionsRow(
    completed: Boolean,
    onDeletePressed: () -> Unit,
    onCompletedPressed: () -> Unit,
    onNavigateToProjectPressed: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .clip(RoundedCornerShape(10.dp)),
        color = MaterialTheme.colorScheme.surface.copy(0.5f),
        contentColor = MaterialTheme.colorScheme.onSurface
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            SingleAction(
                text = stringResource(id = if (completed) R.string.incomplete_project else R.string.complete_project),
                icon = Icon.ImageVectorIcon(FAIcons.check_mark),
                color = if (completed) Color.Green else MaterialTheme.colorScheme.primary,
                onClick = onCompletedPressed,
                modifier = Modifier.size(ACTION_SIZE.dp)
            )
            SingleAction(
                text = stringResource(id = R.string.edit_project),
                icon = Icon.ImageVectorIcon(FAIcons.edit),
                color = MaterialTheme.colorScheme.primary,
                onClick = onNavigateToProjectPressed,
                modifier = Modifier.size(ACTION_SIZE.dp)
            )
            SingleAction(
                text = stringResource(id = R.string.delete_project),
                icon = Icon.DrawableResourceIcon(id = FAIcons.delete),
                color = MaterialTheme.colorScheme.error,
                onClick = onDeletePressed,
                modifier = Modifier.size(ACTION_SIZE.dp)
            )
        }
    }
}

@SuppressLint("UnusedTransitionTargetStateParameter")
@Composable
fun ProjectListCard(
    projectResource: ProjectResource,
    cardOffset: Float,
    navigateToProject: (String) -> Unit,
    onExpand: () -> Unit,
    onCollapse: () -> Unit,
    modifier: Modifier = Modifier,
    isRevealed: Boolean = false
) {

    val transitionState = remember {
        MutableTransitionState(isRevealed).apply {
            targetState = !isRevealed
        }
    }

    val transition = updateTransition(targetState = transitionState, label = "projectListCardTransition")
    val offsetTransition by transition.animateFloat(
        label = "projectListCardOffsetTransition",
        transitionSpec = { tween(ANIMATION_DURATION) },
        targetValueByState = { if (isRevealed) cardOffset else 0f }
    )
    val elevationTransition by transition.animateDp(
        label = "projectListCardElevationTransition",
        transitionSpec = { tween(ANIMATION_DURATION) },
        targetValueByState = { if (isRevealed) 30.dp else 0.dp }
    )

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .offset { IntOffset(-offsetTransition.roundToInt(), 0) }
            .clip(RoundedCornerShape(10.dp))
            .clickable(
                onClick = {
                    if (isRevealed)
                        onCollapse()
                    else
                        navigateToProject(projectResource.project.id)
                }
            )
            .pointerInput(Unit) {
                detectHorizontalDragGestures { _, dragAmount ->
                    when {
                        dragAmount <= -DRAG_AMOUNT -> onExpand()
                        dragAmount > DRAG_AMOUNT -> onCollapse()
                    }
                }
            },
        color = MaterialTheme.colorScheme.primaryContainer,
        contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
        tonalElevation = elevationTransition
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = 8.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            FormIcon(
                color = Color(projectResource.project.color),
                icon = Icon.DrawableResourceIcon(FAIcons.project_form_icon)
            )
            Spacer(modifier = Modifier.size(16.dp))
            Column(
                modifier = Modifier.padding(end = 8.dp),
                verticalArrangement = Arrangement.Center
            ) {
                Row(
                    modifier = Modifier.
                    padding(bottom = 24.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = projectResource.project.name,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1.0f)
                    )
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(percent = 50))
                            .background(Color(projectResource.project.color)),
                        contentAlignment = Alignment.CenterEnd
                    ) {
                        if (projectResource.numberOfTasks > 0) {
                            Text(
                                text = "${projectResource.numberOfTasksCompleted}/${projectResource.numberOfTasks}",
                                style = MaterialTheme.typography.bodySmall,
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 2.dp),
                                color = Color.Black
                            )
                        } else {
                            Text(
                                text = stringResource(id = R.string.project_no_tasks),
                                style = MaterialTheme.typography.labelSmall,
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 2.dp),
                                color = Color.Black,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis

                            )
                        }
                    }
                }
                Text(
                    text = projectResource.tagName,
                    style = MaterialTheme.typography.labelSmall,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.size(4.dp))
                if (projectResource.numberOfTasks > 0) {
                    ProjectProgress(
                        numberOfTasks = projectResource.numberOfTasks,
                        numberOfCompletedTasks = projectResource.numberOfTasksCompleted,
                        color = Color(projectResource.project.color),
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                Spacer(modifier = Modifier.size(6.dp))
                DisplayTime(date = projectResource.project.createdAt)
            }
        }
    }
}

@Composable
fun ProjectProgress(
    numberOfTasks: Int,
    numberOfCompletedTasks: Int,
    color: Color,
    modifier: Modifier = Modifier,
    backgroundColor: Color = MaterialTheme.colorScheme.onPrimaryContainer.copy(0.1f)
) {
    Canvas(
        modifier = modifier
            .height(6.dp)
    ) {
        val completionPercentage: Float = (numberOfCompletedTasks/numberOfTasks.toFloat()) * size.width

        drawLine(
            color = backgroundColor,
            cap = StrokeCap.Round,
            strokeWidth = size.height,
            start = Offset(x = 0f, y = 12f),
            end = Offset(x = size.width, y = 12f)
        )

        if (numberOfCompletedTasks > 0) {
            drawLine(
                color = color,
                cap = StrokeCap.Round,
                strokeWidth = size.height,
                start = Offset(x = 0f, y = 12f),
                end = Offset(x = completionPercentage, y = 12f)
            )
        }
    }
}

@ThemePreviews
@Composable
fun ProjectListCardDarkPreview() {
    FocusedAppTheme {
        ProjectListCard(
            projectResource = ProjectResource(
                project = Project(
                    name = "Design App Landing Screen",
                    color = Color(0xFFD5ABEF).toArgb(),
                    createdAt = OffsetDateTime.now().plusDays(5)
                ),
                tagName = "Design",
                numberOfTasks = 0,
                numberOfTasksCompleted = 0
            ),
            navigateToProject = {},
            onExpand = {},
            onCollapse = {},
            cardOffset = 300f
        )
    }
}

@ThemePreviews
@Composable
fun ProjectListCardProgressDarkPreview() {
    FocusedAppTheme {
        ProjectListCard(
            projectResource = ProjectResource(
                project = Project(
                    name = "Merge All Pull Requests",
                    color = Color.Magenta.toArgb(),
                    createdAt = OffsetDateTime.now().plusDays(150)
                ),
                tagName = "Code",
                numberOfTasks = 19,
                numberOfTasksCompleted = 3
            ),
            navigateToProject = {},
            onExpand = {},
            onCollapse = {},
            cardOffset = 300f
        )
    }
}