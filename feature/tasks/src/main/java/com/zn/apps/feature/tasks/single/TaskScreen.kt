package com.zn.apps.feature.tasks.single

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.zn.apps.common.millisecondsToMinutes
import com.zn.apps.common.minutesToMilliseconds
import com.zn.apps.feature.tasks.R
import com.zn.apps.model.data.task.Task
import com.zn.apps.model.data.task.TaskPriority
import com.zn.apps.ui_common.delegate.TasksUiStateHolder
import com.zn.apps.ui_design.component.DropDownItem
import com.zn.apps.ui_design.component.FATextInputSelector
import com.zn.apps.ui_design.component.FATopAppBar
import com.zn.apps.ui_design.component.ThemePreviews
import com.zn.apps.ui_design.component.dialog.DeadlineSelectionDialog
import com.zn.apps.ui_design.component.dialog.PomodoroDialog
import com.zn.apps.ui_design.component.dialog.SimpleConfirmationDialog
import com.zn.apps.ui_design.component.formatDateForUi
import com.zn.apps.ui_design.icon.FAIcons
import com.zn.apps.ui_design.icon.Icon
import com.zn.apps.ui_design.theme.FocusedAppTheme
import com.zn.apps.ui_design.util.uiColor
import java.time.OffsetDateTime
import com.zn.apps.ui_design.R as uiDesignR

@Composable
fun TaskRoute(
    taskUiModel: TaskUiModel,
    uiStateHolder: TasksUiStateHolder,
    onUpdatePressed: (Task) -> Unit,
    onDeletePressed: () -> Unit,
    onDeleteDismissed: () -> Unit,
    onDeleteConfirmed: (Task) -> Unit,
    onUpPressed: () -> Unit
) {

    if (uiStateHolder.showDeleteTaskDialog) {
        SimpleConfirmationDialog(
            title = uiDesignR.string.delete,
            text = uiDesignR.string.delete_task_message,
            confirmationAction = { onDeleteConfirmed(taskUiModel.task) },
            cancelAction = { onDeleteDismissed() }
        )
    }

    TaskScreen(
        task = taskUiModel.task,
        uiStateHolder = uiStateHolder,
        onUpdatePressed = onUpdatePressed,
        onDeletePressed = onDeletePressed,
        onUpPressed = onUpPressed
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskScreen(
    task: Task,
    uiStateHolder: TasksUiStateHolder,
    onUpdatePressed: (Task) -> Unit,
    onDeletePressed: () -> Unit,
    onUpPressed: () -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        topBar = {
            FATopAppBar(
                titleName = stringResource(id = R.string.edit_task),
                navigationIcon = Icon.ImageVectorIcon(FAIcons.ArrowBack),
                onNavigationIconClicked =  onUpPressed,
                actions = {
                    IconButton(onClick = { onDeletePressed() }) {
                        Icon(
                            painter = painterResource(id = FAIcons.delete),
                            contentDescription = stringResource(id = R.string.delete_task),
                            tint = MaterialTheme.colorScheme.error,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
            ) {}
        },
        modifier = modifier
    ) {
        TaskContent(
            task = task,
            uiStateHolder = uiStateHolder,
            onUpdatePressed = onUpdatePressed,
            modifier = Modifier.padding(it)
        )
    }
}

@Composable
fun TaskContent(
    task: Task,
    uiStateHolder: TasksUiStateHolder,
    onUpdatePressed: (Task) -> Unit,
    modifier: Modifier
) {
    val (formState, formStateSetter) = remember {
        mutableStateOf(
            TaskFormState(
                name = task.name,
                priority = task.priority,
                selectedTagId = task.tagId,
                selectedProjectId = task.projectId,
                pomodoroLength = task.pomodoro.pomodoroLength.millisecondsToMinutes(),
                pomodoroNumber = task.pomodoro.pomodoroNumber,
                dueDate = task.dueDate
            )
        )
    }
    var showDueDateDialog by remember { mutableStateOf(false) }
    var showPomodoroDialog by remember { mutableStateOf(false) }

    if (showPomodoroDialog) {
        PomodoroDialog(
            pomodoroNumber = formState.pomodoroNumber,
            pomodoroLength = formState.pomodoroLength,
            setShowDialog = { showPomodoroDialog = it },
            onSave = {
                formStateSetter(
                    formState.copy(
                        pomodoroNumber = it.pomodoroNumber,
                        pomodoroLength = it.pomodoroLength
                    )
                )
            }
        )
    }
    if (showDueDateDialog) {
        DeadlineSelectionDialog(
            dateTime = formState.dueDate,
            onDateTimeSelected = {
                formStateSetter(
                    formState.copy(
                        dueDate = it
                    )
                )
            },
            onDismissRequest = { showDueDateDialog = it }
        )
    }

    Surface(
        modifier = modifier
            .fillMaxWidth()
    ) {
        val project = uiStateHolder.projects.firstOrNull { it.id == formState.selectedProjectId }
            
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(24.dp),
            modifier = Modifier
                .padding(horizontal = 12.dp)
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
            ) {
                TaskTextField(
                    name = formState.name,
                    priority = formState.priority,
                    completed = task.completed,
                    tags = uiStateHolder.tags,
                    onNameChanged = {
                        formStateSetter(
                            formState.copy(
                                name = it
                            )
                        )
                    },
                    onPriorityChanged = {
                        formStateSetter(
                            formState.copy(
                                priority = it
                            )
                        )
                    },
                    onSelectedTagIdChanged = {
                        formStateSetter(
                            formState.copy(
                                selectedTagId = it
                            )
                        )
                    },
                    selectedTagId = formState.selectedTagId,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp)
                )
                FATextInputSelector(
                    title = project?.name ?: stringResource(id = R.string.select_project),
                    leadingIcon = {
                        Icon(
                            painter = painterResource(id = FAIcons.project_form_icon),
                            contentDescription = null,
                            tint = project?.uiColor() ?: Color.Gray,
                            modifier = Modifier.size(20.dp)
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                ) { onClick ->
                    uiStateHolder.projects.forEach { project ->
                        DropDownItem(
                            item = project,
                            onItemSelected = { item ->
                                formStateSetter(
                                    formState.copy(
                                        selectedProjectId = item.id
                                    )
                                )
                            },
                            onDropDownExpanded = { onClick() },
                            text = project.name,
                            color = project.uiColor()
                        )
                    }
                }
                PomodoroRow(
                    pomodoroNumber = formState.pomodoroNumber,
                    pomodoroLength = formState.pomodoroLength,
                    onClick = { showPomodoroDialog = true },
                    modifier = Modifier.padding(vertical = 8.dp)
                )
                DueDateRow(
                    dueDate = formState.dueDate,
                    onClick = { showDueDateDialog = true },
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }
            Button(
                onClick = {
                    val taskToUpdate = task.copy(
                        name = formState.name.ifBlank { task.name },
                        pomodoro = task.pomodoro.copy(
                            pomodoroNumber = formState.pomodoroNumber,
                            pomodoroLength = formState.pomodoroLength.minutesToMilliseconds()
                        ),
                        priority = formState.priority,
                        projectId = formState.selectedProjectId,
                        tagId = formState.selectedTagId,
                        dueDate = formState.dueDate
                    )
                    onUpdatePressed(taskToUpdate)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .align(Alignment.CenterHorizontally)
            ) {
                Text(text = stringResource(id = R.string.update_task))
            }
        }
    }
}

@Composable
fun PomodoroRow(
    pomodoroNumber: Int,
    pomodoroLength: Int,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .clickable { onClick() }
            .padding(vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            modifier = Modifier
                .weight(1f),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(id = FAIcons.pomodoro),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(text = stringResource(id = R.string.pomodoro))
        }
        Column(
            verticalArrangement = Arrangement.Center
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(id = FAIcons.pomodoro),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(10.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "$pomodoroNumber",
                    style = MaterialTheme.typography.labelSmall
                )
            }
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(id = FAIcons.pomodoro),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(10.dp)
                )
                Spacer(modifier = Modifier.width(2.dp))
                Text(
                    text = "= ${pomodoroLength}m",
                    style = MaterialTheme.typography.labelSmall
                )
            }
        }
    }
}

@Composable
fun DueDateRow(
    dueDate: OffsetDateTime?,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .clickable { onClick() }
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            modifier = Modifier
                .weight(1f),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(id = FAIcons.calendar),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(text = stringResource(id = R.string.due_date))
        }
        if (dueDate != null) {
            Text(
                text = formatDateForUi(dateTime = dueDate),
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.tertiary
            )
        } else {
            Text(text = stringResource(id = R.string.no_date))
        }
    }
}

@Immutable
data class TaskFormState(
    var selectedProjectId: String? = null,
    var selectedTagId: String? = null,
    var name: String = "",
    var priority: TaskPriority = TaskPriority.NONE,
    var pomodoroNumber: Int = 0,
    var pomodoroLength: Int = 0,
    var dueDate: OffsetDateTime? = null
)

@ThemePreviews
@Composable
fun PomodoroRowPreview() {
    FocusedAppTheme {
        Surface {
            PomodoroRow(
                pomodoroNumber = 5,
                pomodoroLength = 30,
                onClick = {  })
        }
    }
}

@ThemePreviews
@Composable
fun DueDateRowPreview() {
    FocusedAppTheme {
        Surface {
            DueDateRow(
                dueDate = OffsetDateTime.now().plusDays(5),
                onClick = { }
            )
        }
    }
}