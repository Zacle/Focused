package com.zn.apps.feature.tasks.completed

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.zn.apps.model.data.task.Pomodoro
import com.zn.apps.model.data.task.Task
import com.zn.apps.model.data.task.TaskResource
import com.zn.apps.ui_design.component.FocusedAppBackground
import com.zn.apps.ui_design.component.TaskCard
import com.zn.apps.ui_design.component.ThemePreviews
import com.zn.apps.ui_design.icon.FAIcons
import com.zn.apps.ui_design.theme.FocusedAppTheme
import java.time.OffsetDateTime

@Composable
fun TimelineNode(
    title: String,
    subtitle: String,
    circleParameters: CircleParameters,
    completedTasks: List<TaskResource>,
    onTaskPressed: (String) -> Unit,
    onSetTaskUnCompleted: (Task) -> Unit,
    modifier: Modifier = Modifier,
    lineParameters: LineParameters? = null,
    contentStartOffset: Dp = 32.dp,
    spaceBetweenNodes: Dp = 32.dp,
    isNodeLast: Boolean = false
) {
    val iconPainter = painterResource(id = FAIcons.timeline_icon)
    val iconTint: Color = MaterialTheme.colorScheme.onPrimary

    Box(
        modifier = modifier
            .padding(start = 8.dp)
            .wrapContentSize()
            .drawBehind {
                val circleRadiusInPx = circleParameters.radius.toPx()
                drawCircle(
                    color = circleParameters.backgroundColor,
                    radius = circleRadiusInPx,
                    center = Offset(circleRadiusInPx, circleRadiusInPx * 1.5f)
                )
                lineParameters?.let {
                    drawLine(
                        brush = lineParameters.brush,
                        start = Offset(x = circleRadiusInPx, y = circleRadiusInPx * 2),
                        end = Offset(x = circleRadiusInPx, y = this.size.height),
                        strokeWidth = lineParameters.strokeWidth.toPx()
                    )
                }
                iconPainter.let { painter ->
                    this.withTransform(
                        transformBlock = {
                            translate(
                                left = circleRadiusInPx - painter.intrinsicSize.width / 2,
                                top = circleRadiusInPx - painter.intrinsicSize.height / 5
                            )
                        },
                        drawBlock = {
                            this.drawIntoCanvas {
                                with(painter) {
                                    draw(
                                        size = intrinsicSize,
                                        colorFilter = ColorFilter.tint(color = iconTint)
                                    )
                                }
                            }
                        })
                }
            }
    ) {
        Column(
            modifier = Modifier
                .padding(
                    start = contentStartOffset,
                    bottom = if (isNodeLast) 0.dp else spaceBetweenNodes
                )
        ) {
            TimelineHeader(
                title = title,
                subtitle = subtitle
            )

            completedTasks.forEachIndexed { index, taskResource ->
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
                        .padding(
                            bottom = if (index == completedTasks.size - 1) 0.dp else 4.dp
                        )
                )
            }
        }
    }
}

@Composable
fun TimelineHeader(
    title: String,
    subtitle: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .padding(bottom = 16.dp)
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = subtitle,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
        )
    }
}

@ThemePreviews
@Composable
fun TimelineNodePreview() {
    FocusedAppTheme {
        FocusedAppBackground {
            TimelineNode(
                title = "Tue, 4 October",
                subtitle = "Focus: 5h50m, Completed: 3 tasks",
                completedTasks = listOf(
                    TaskResource(
                        task = Task(
                            name = "Study Math",
                            pomodoro = Pomodoro(pomodoroNumber = 4, pomodoroCompleted = 4),
                            dueDate = OffsetDateTime.now(),
                            completed = true
                        )
                    ),
                    TaskResource(
                        task = Task(
                            name = "Study Kotlin",
                            pomodoro = Pomodoro(pomodoroNumber = 4, pomodoroCompleted = 4),
                            dueDate = OffsetDateTime.now(),
                            completed = true
                        )
                    ),
                    TaskResource(
                        task = Task(
                            name = "Walk dogs",
                            pomodoro = Pomodoro(pomodoroNumber = 4, pomodoroCompleted = 3),
                            dueDate = OffsetDateTime.now(),
                            completed = true
                        )
                    )
                ),
                onTaskPressed = {},
                onSetTaskUnCompleted = {},
                circleParameters = CircleParametersDefaults.circleParameters(
                    backgroundColor = MaterialTheme.colorScheme.primary
                ),
                lineParameters = LineParametersDefaults.linearGradient(
                    startColor = MaterialTheme.colorScheme.primary,
                    endColor = MaterialTheme.colorScheme.secondary
                )
            )
        }
    }
}