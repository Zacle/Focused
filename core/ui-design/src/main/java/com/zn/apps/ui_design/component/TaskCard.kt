package com.zn.apps.ui_design.component

import android.annotation.SuppressLint
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Green
import androidx.compose.ui.graphics.Color.Companion.Transparent
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.zn.apps.model.data.task.Pomodoro
import com.zn.apps.model.data.task.Task
import com.zn.apps.ui_design.icon.FAIcons
import com.zn.apps.ui_design.theme.FocusedAppTheme
import java.time.OffsetDateTime
import kotlin.math.roundToInt

const val CARD_OFFSET = 300f
const val ANIMATION_DURATION = 600
const val TASK_PROGRESS_WIDTH_COMPACT = 130
const val DRAG_AMOUNT = 6


@SuppressLint("UnusedTransitionTargetStateParameter")
@Composable
fun TaskCard(
    task: Task,
    isTaskRunning: Boolean,
    cardOffset: Float,
    navigateToTask: (String) -> Unit,
    onCompleted: (Task) -> Unit,
    onStartTask: () -> Unit,
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
    val transition = updateTransition(targetState = transitionState, label = "taskCardTransition")
    val offsetTransition by transition.animateFloat(
        label = "taskCardOffsetTransition",
        transitionSpec = { tween(ANIMATION_DURATION) },
        targetValueByState = { if (isRevealed) cardOffset else -0f }
    )
    val elevationTransition by transition.animateDp(
        label = "taskCardElevationTransition",
        transitionSpec = { tween(ANIMATION_DURATION) },
        targetValueByState = { if (isRevealed) 40.dp else 2.dp }
    )

    val interactionSource = remember { MutableInteractionSource() }

    var completed by remember {
        mutableStateOf(false)
    }

    val border =
        if (isTaskRunning) {
            Modifier.border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.secondary,
                shape = RoundedCornerShape(10.dp)
            )
        } else {
            Modifier.border(
                width = 0.dp,
                color = Transparent
            )
        }

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .offset { IntOffset(-offsetTransition.roundToInt(), 0) }
            .clip(RoundedCornerShape(10.dp))
            .then(border)
            .clickable(onClick = {
                if (isRevealed) {
                    onCollapse()
                } else {
                    navigateToTask(task.id)
                }
            })
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
                .padding(horizontal = 12.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .toggleable(
                        value = completed,
                        interactionSource = interactionSource,
                        onValueChange = {
                            completed = !completed
                            onCompleted(task)
                        },
                        indication = null
                    )
                    .border(
                        width = 1.dp,
                        color = MaterialTheme.colorScheme.secondary,
                        shape = CircleShape
                    )
            ) {
                val color by animateColorAsState(
                    if (completed) Green.copy(0.6f) else Green.copy(0.0f),
                    label = "completed color"
                )
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = null,
                    tint = color
                )
            }
            Column(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .weight(1.0f),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = task.name,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.bodySmall,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                if (task.pomodoro.pomodoroNumber > 0) {
                    Row(
                        modifier = Modifier
                            .padding(top = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        TaskProgress(
                            pomodoroNumber = task.pomodoro.pomodoroNumber,
                            pomodoroCompleted = task.pomodoro.pomodoroCompleted,
                            modifier = Modifier
                                .width(TASK_PROGRESS_WIDTH_COMPACT.dp)
                        )

                        Row(modifier = Modifier
                            .padding(start = 2.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                painter = painterResource(id = FAIcons.pomodoro),
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.secondary
                            )
                            Text(
                                text = "${task.pomodoro.pomodoroCompleted}/${task.pomodoro.pomodoroNumber}",
                                modifier = Modifier.padding(start = 8.dp),
                                color = MaterialTheme.colorScheme.secondary,
                                style = MaterialTheme.typography.labelSmall
                            )
                        }
                    }
                }
                if (task.dueDate != null) {
                    DisplayTime(date = task.dueDate!!)
                }
            }
            IconButton(
                onClick = { onStartTask() }
            ) {
                Icon(
                    painter = painterResource(
                        id = if (isTaskRunning) FAIcons.pomodoro else FAIcons.play_circle
                    ),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.secondary,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}

@Composable
fun TaskProgress(
    pomodoroNumber: Int,
    pomodoroCompleted: Int,
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.onPrimaryContainer.copy(0.1f)
) {

    val percentageColor = MaterialTheme.colorScheme.tertiary

    Canvas(
        modifier = modifier
            .height(6.dp)
            .padding(end = 16.dp)
    ) {

        val completionPercentage: Float = (pomodoroCompleted/ pomodoroNumber.toFloat()) * size.width

        drawLine(
            color = color,
            cap = StrokeCap.Round,
            strokeWidth = size.height,
            start = Offset(x = 0f, y = 12f),
            end = Offset(x = size.width, y = 12f)
        )

        if (pomodoroCompleted > 0) {
            drawLine(
                color = percentageColor,
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
fun TaskCardPreview() {
    FocusedAppTheme {
        TaskCard(
            task = Task(
                name = "Wireframe",
                pomodoro = Pomodoro(pomodoroNumber = 5, pomodoroCompleted = 2)
            ),
            cardOffset = CARD_OFFSET,
            navigateToTask = {},
            onCompleted = {},
            onStartTask = {},
            isTaskRunning = true,
            onExpand = {},
            onCollapse = {}
        )
    }
}

@ThemePreviews
@Composable
fun TaskCardNotRunningPreview() {
    FocusedAppTheme {
        TaskCard(
            task = Task(
                name = "Wireframe",
                pomodoro = Pomodoro(pomodoroNumber = 5, pomodoroCompleted = 4),
                dueDate = OffsetDateTime.now().plusDays(3)
            ),
            cardOffset = CARD_OFFSET,
            navigateToTask = {},
            onCompleted = {},
            onStartTask = {},
            isTaskRunning = false,
            onExpand = {},
            onCollapse = {}
        )
    }
}