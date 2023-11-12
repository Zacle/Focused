package com.zn.apps.feature.settings.sound_and_notification

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.zn.apps.feature.settings.R
import com.zn.apps.feature.settings.SettingsTextField
import com.zn.apps.feature.settings.SettingsTextFieldSwitch
import com.zn.apps.feature.settings.SettingsTextFiledDropdown
import com.zn.apps.model.datastore.ReminderPreferences
import com.zn.apps.ui_design.component.FATopAppBar
import com.zn.apps.ui_design.component.dialog.SnoozeReminderDialog
import com.zn.apps.ui_design.icon.FAIcons
import com.zn.apps.ui_design.icon.Icon

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SoundNotificationSettingsRoute(
    soundNotificationSettingsUiModel: SoundNotificationSettingsUiModel,
    onUpPressed: () -> Unit,
    onSetTaskReminderMinutes: (Int) -> Unit,
    onSetDailyTodoReminder: (Boolean) -> Unit,
    onSetSnoozeTaskReminder: (Boolean) -> Unit,
    onSetSnoozeTaskReminderAfter: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        topBar = {
            FATopAppBar(
                titleName = stringResource(id = R.string.sound_notification_settings),
                navigationIcon = Icon.ImageVectorIcon(FAIcons.ArrowBack),
                onNavigationIconClicked = onUpPressed
            ) {}
        },
        modifier = modifier
    ) { innerPadding ->
        SoundNotificationSettingsScreen(
            reminderPreferences = soundNotificationSettingsUiModel.reminderPreferences,
            onSetTaskReminderMinutes = onSetTaskReminderMinutes,
            onSetDailyTodoReminder = onSetDailyTodoReminder,
            onSetSnoozeTaskReminder = onSetSnoozeTaskReminder,
            onSetSnoozeTaskReminderAfter = onSetSnoozeTaskReminderAfter,
            modifier = Modifier
                .padding(innerPadding)
                .padding(
                    top = 32.dp,
                    start = 16.dp,
                    end = 16.dp
                )
        )
    }
}

@Composable
fun SoundNotificationSettingsScreen(
    reminderPreferences: ReminderPreferences,
    onSetTaskReminderMinutes: (Int) -> Unit,
    onSetDailyTodoReminder: (Boolean) -> Unit,
    onSetSnoozeTaskReminder: (Boolean) -> Unit,
    onSetSnoozeTaskReminderAfter: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    var showSnoozeDialog by remember {
        mutableStateOf(false)
    }
    Surface(modifier) {
        Column(
            verticalArrangement = Arrangement.Center
        ) {
            SettingsTextFiledDropdown(
                title = stringResource(id = R.string.task_reminder_default),
                taskReminderAfter = reminderPreferences.taskReminder,
                onClicked = onSetTaskReminderMinutes
            )
            SettingsTextFieldSwitch(
                title = stringResource(id = R.string.todo_reminder),
                checked = reminderPreferences.todoReminder,
                onCheckedChanged = onSetDailyTodoReminder
            )
            SettingsTextField(
                title = stringResource(id = R.string.snooze_task_reminder),
                selection =
                    if (reminderPreferences.snoozeTaskReminder)
                        stringResource(id = R.string.on)
                    else
                        stringResource(id = R.string.off),
                onClicked = { showSnoozeDialog = true }
            )
        }
    }
    if (showSnoozeDialog) {
        SnoozeReminderDialog(
            snoozeReminderAt = reminderPreferences.snoozeAfter,
            isSnoozeReminderSet = reminderPreferences.snoozeTaskReminder,
            onSnoozeReminderAtSet = onSetSnoozeTaskReminderAfter,
            setSnoozeReminder = onSetSnoozeTaskReminder,
            setShowDialog = { showSnoozeDialog = false }
        )
    }
}
