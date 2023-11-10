package com.zn.apps.ui_common

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.zn.apps.common.minutesToMilliseconds
import com.zn.apps.model.data.project.Project
import com.zn.apps.model.data.tag.Tag
import com.zn.apps.model.data.task.Pomodoro
import com.zn.apps.model.data.task.Task
import com.zn.apps.model.data.task.TaskPriority
import com.zn.apps.ui_design.R
import com.zn.apps.ui_design.component.ChoosePriority
import com.zn.apps.ui_design.component.ChooseTag
import com.zn.apps.ui_design.component.FAInputSelector
import com.zn.apps.ui_design.component.FATextInput
import com.zn.apps.ui_design.component.SelectProject
import com.zn.apps.ui_design.component.dialog.DeadlineSelectionDialog
import com.zn.apps.ui_design.component.dialog.PomodoroDialog
import com.zn.apps.ui_design.component.dialog.getEstimatedTime
import com.zn.apps.ui_design.icon.FAIcons
import com.zn.apps.ui_design.icon.Icon
import java.time.OffsetDateTime

@Composable
fun InsertTaskBottomSheetContent(
    upsertTask: (Task) -> Unit,
    projects: List<Project>,
    taskReminder: Int,
    tags: List<Tag>,
    shouldShowModalSheet: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    val (formState, formStateSetter) = remember { mutableStateOf(FormState(remindBefore = taskReminder)) }
    var showPomodoroDialog by remember { mutableStateOf(false) }
    var showDueDateDialog by remember { mutableStateOf(false) }

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
            onDateTimeWithReminderSet = { dueDate, remindBefore, isReminderSet ->
                formStateSetter(
                    formState.copy(
                        dueDate = dueDate,
                        remindBefore = remindBefore,
                        isReminderSet = isReminderSet
                    )
                )
            },
            onDismissRequest = { showDueDateDialog = false },
            isReminderSet = formState.dueDate != null,
            remindBeforeValue = taskReminder
        )
    }

    Surface(
        color = Color.Transparent,
        modifier = Modifier
            .navigationBarsPadding()
    ) {
        Column(
            modifier = modifier
                .padding(8.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            /**
             * Select Project name to associate this task with
             */
            SelectProject(
                projectId = formState.selectedProjectId,
                projects = projects,
                onProjectIdSelected = {
                    formStateSetter(
                        formState.copy(selectedProjectId = it)
                    )
                }
            )
            /**
             * Enter the task name
             */
            FATextInput(
                name = formState.name,
                placeholder = stringResource(id = R.string.enter_task_name),
                icon = Icon.DrawableResourceIcon(FAIcons.task_name),
                onValueChange = {
                    formStateSetter(
                        formState.copy(name = it)
                    )
                }
            )
            /**
             * Select the pomodoro number and length. Used to estimate the time it will take
             * to complete the task
             */
            FAInputSelector(
                backgroundColor = Color.Transparent,
                onClick = { showPomodoroDialog = true },
                text = stringResource(id = R.string.select_pomodoro),
                icon = Icon.DrawableResourceIcon(FAIcons.pomodoro),
                label = getEstimatedTime(
                    pomodoroNumber = formState.pomodoroNumber,
                    pomodoroLength = formState.pomodoroLength
                )
            )

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    modifier = Modifier.weight(1.0f),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    /**
                     * Select tag to associate this task with
                     */
                    ChooseTag(
                        tags = tags,
                        selectedTagId = formState.selectedTagId,
                        onTagSelected = {
                            formStateSetter(
                                formState.copy(selectedTagId = it?.id)
                            )
                        }
                    )
                    /**
                     * Select due date and time
                     */
                    IconButton(onClick = { showDueDateDialog = true }) {
                        Icon(
                            painter = painterResource(id = FAIcons.calendar),
                            contentDescription = stringResource(id = R.string.select_due_date),
                            tint = MaterialTheme.colorScheme.secondary.copy(alpha = 0.5f),
                            modifier = Modifier.size(24.dp)
                        )
                    }
                    /**
                     * Select priority
                     */
                    ChoosePriority(
                        onPrioritySelected = {
                            formStateSetter(
                                formState.copy(priority = it)
                            )
                        },
                        selectedPriority = formState.priority
                    )
                }
                /**
                 * Save task to the database
                 */
                TextButton(
                    onClick = {
                        val task = Task(
                            name = formState.name,
                            pomodoro = Pomodoro(
                                pomodoroNumber = formState.pomodoroNumber,
                                pomodoroLength = formState.pomodoroLength.minutesToMilliseconds()
                            ),
                            priority = formState.priority,
                            projectId = formState.selectedProjectId,
                            tagId = formState.selectedTagId,
                            dueDate = formState.dueDate,
                            remindTaskAt = formState.remindBefore,
                            shouldRemindTask = formState.isReminderSet
                        )
                        upsertTask(task)
                        shouldShowModalSheet(false)
                    },
                    enabled = formState.name.isNotBlank()
                ) {
                    Text(
                        text = stringResource(id = R.string.done),
                        style = MaterialTheme.typography.labelLarge,
                        color =
                            if (formState.name.isNotBlank())
                                MaterialTheme.colorScheme.primary
                            else
                                Color.Gray,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Immutable
data class FormState(
    var selectedProjectId: String? = null,
    var selectedTagId: String? = null,
    var name: String = "",
    var priority: TaskPriority = TaskPriority.NONE,
    var pomodoroNumber: Int = 0,
    var pomodoroLength: Int = 0,
    var dueDate: OffsetDateTime? = OffsetDateTime.now(),
    var remindBefore: Int = 0,
    var isReminderSet: Boolean = true
)