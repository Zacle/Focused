package com.zn.apps.ui_design.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.zn.apps.common.millisecondsToMinutes
import com.zn.apps.model.data.task.Pomodoro
import com.zn.apps.model.data.task.Task
import com.zn.apps.ui_design.R
import com.zn.apps.ui_design.component.dialog.DeadlineSelectionDialog
import com.zn.apps.ui_design.component.dialog.PomodoroDialog
import com.zn.apps.ui_design.component.dialog.SimpleConfirmationDialog
import com.zn.apps.ui_design.component.dialog.UiPomodoroState
import com.zn.apps.ui_design.icon.FAIcons
import com.zn.apps.ui_design.theme.FocusedAppTheme
import java.time.OffsetDateTime

const val ACTION_SIZE = 50

@Composable
fun DraggableTaskCard(
    task: Task,
    isTaskRunning: Boolean,
    showDeleteDialog: Boolean,
    showDueDateDialog: Boolean,
    showPomodoroDialog: Boolean,
    isTaskRevealed: Boolean,
    navigateToTask: (String) -> Unit,
    onCompleted: (Task) -> Unit,
    onStartTask: () -> Unit,
    onDeleteTaskPressed: () -> Unit,
    onDeleteTaskDismissed: () -> Unit,
    onDeleteTaskConfirmed: () -> Unit,
    onUpdateDueDatePressed: () -> Unit,
    onUpdateDueDateDismissed: () -> Unit,
    onUpdateDueDateConfirmed: (OffsetDateTime?) -> Unit,
    onUpdatePomodoroPressed: () -> Unit,
    onUpdatePomodoroDismissed: () -> Unit,
    onUpdatePomodoroConfirmed: (UiPomodoroState) -> Unit,
    onExpand: () -> Unit,
    onCollapse: () -> Unit,
    modifier: Modifier = Modifier
) {

    var cardOffset by remember {
        mutableFloatStateOf(0f)
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        ActionsRow(
            onDeletePressed = onDeleteTaskPressed,
            onDueDatePressed = onUpdateDueDatePressed,
            onPomodoroPressed = onUpdatePomodoroPressed,
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .onGloballyPositioned {
                    cardOffset = it.size.width.toFloat()
                }
        )
        TaskCard(
            task = task,
            isTaskRunning = isTaskRunning,
            navigateToTask = navigateToTask,
            onCompleted = onCompleted,
            onStartTask = onStartTask,
            onExpand = onExpand,
            onCollapse = onCollapse,
            isRevealed = isTaskRevealed,
            cardOffset = cardOffset,
            modifier = modifier.padding(top = 5.dp)
        )
    }

    if (isTaskRevealed) {
        if (showDeleteDialog) {
            SimpleConfirmationDialog(
                title = R.string.delete,
                text = R.string.delete_task_message,
                confirmationAction = onDeleteTaskConfirmed,
                cancelAction = onDeleteTaskDismissed
            )
        }
        if (showDueDateDialog) {
            DeadlineSelectionDialog(
                dateTime = task.dueDate,
                onDateTimeSelected = { onUpdateDueDateConfirmed(it) },
                onDismissRequest = { onUpdateDueDateDismissed() }
            )
        }
        if (showPomodoroDialog) {
            PomodoroDialog(
                pomodoroNumber = task.pomodoro.pomodoroNumber,
                pomodoroLength = task.pomodoro.pomodoroLength.millisecondsToMinutes(),
                setShowDialog = { onUpdatePomodoroDismissed() },
                onSave = {
                    onUpdatePomodoroConfirmed(it)
                }
            )
        }
    }
}

@Composable
fun ActionsRow(
    onDeletePressed: () -> Unit,
    onDueDatePressed: () -> Unit,
    onPomodoroPressed: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .clip(RoundedCornerShape(10.dp)),
        color = Color.Transparent,
        contentColor = MaterialTheme.colorScheme.onSurface
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            SingleAction(
                text = stringResource(id = R.string.pomodoro),
                painter = painterResource(id = FAIcons.pomodoro),
                color = MaterialTheme.colorScheme.primary,
                onClick = onPomodoroPressed,
                modifier = Modifier.size(ACTION_SIZE.dp)
            )
            SingleAction(
                text = stringResource(id = R.string.due_date),
                painter = painterResource(id = FAIcons.calendar),
                color = MaterialTheme.colorScheme.primary,
                onClick = onDueDatePressed,
                modifier = Modifier.size(ACTION_SIZE.dp)
            )
            SingleAction(
                text = stringResource(id = R.string.delete),
                painter = painterResource(id = FAIcons.delete),
                color = MaterialTheme.colorScheme.error,
                onClick = onDeletePressed,
                modifier = Modifier.size(ACTION_SIZE.dp)
            )
        }
    }
}

@Composable
fun SingleAction(
    text: String,
    painter: Painter,
    color: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ) { onClick() },
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .clip(CircleShape)
                .background(color.copy(alpha = 0.1f))
                .padding(6.dp),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painter,
                contentDescription = text,
                tint = color,
                modifier = Modifier.size(24.dp)
            )
        }
    }
}

@ThemePreviews
@Composable
fun ActionsRowPreview() {
    FocusedAppTheme {
        ActionsRow(
            onDeletePressed = { /*TODO*/ },
            onDueDatePressed = { /*TODO*/ },
            onPomodoroPressed = {}
        )
    }
}

@ThemePreviews
@Composable
fun DraggableTaskCardPreview() {
    FocusedAppTheme {
        DraggableTaskCard(
            task = Task(
                name = "UX Design", dueDate = OffsetDateTime.now(), pomodoro = Pomodoro(
                    pomodoroNumber = 6,
                    pomodoroCompleted = 1
                )
            ),
            isTaskRunning = false,
            showDeleteDialog = false,
            showDueDateDialog = false,
            showPomodoroDialog = false,
            isTaskRevealed = false,
            navigateToTask = {},
            onCompleted = {},
            onStartTask = {  },
            onDeleteTaskPressed = {  },
            onDeleteTaskDismissed = {  },
            onDeleteTaskConfirmed = {  },
            onUpdateDueDatePressed = {  },
            onUpdateDueDateDismissed = {  },
            onUpdateDueDateConfirmed = {},
            onUpdatePomodoroPressed = {  },
            onUpdatePomodoroDismissed = {  },
            onUpdatePomodoroConfirmed = {},
            onExpand = {},
            onCollapse = {}
        )
    }
}