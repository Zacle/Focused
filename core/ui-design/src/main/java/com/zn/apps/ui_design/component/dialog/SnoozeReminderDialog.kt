package com.zn.apps.ui_design.component.dialog

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.zn.apps.ui_design.R
import com.zn.apps.ui_design.component.DropDownItem
import com.zn.apps.ui_design.component.ThemePreviews
import com.zn.apps.ui_design.icon.FAIcons
import com.zn.apps.ui_design.theme.FocusedAppTheme

@Composable
fun SnoozeReminderDialog(
    snoozeReminderAt: Int,
    isSnoozeReminderSet: Boolean,
    onSnoozeReminderAtSet: (Int) -> Unit,
    setSnoozeReminder: (Boolean) -> Unit,
    setShowDialog: () -> Unit,
    modifier: Modifier = Modifier
) {
    var remindBeforeValue by remember {
        mutableIntStateOf(snoozeReminderAt)
    }
    var reminderSet by remember {
        mutableStateOf(isSnoozeReminderSet)
    }
    var expanded by remember {
        mutableStateOf(false)
    }

    Dialog(
        onDismissRequest = setShowDialog
    ) {
        Surface(
            modifier = modifier
                .fillMaxWidth()
                .padding(8.dp),
            shape = RoundedCornerShape(20.dp),
            color = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.onSurface
        ) {
            Column(
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .padding(8.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = stringResource(id = R.string.snooze_task_reminder),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier
                            .weight(1f)
                    )
                    Switch(
                        checked = reminderSet,
                        onCheckedChange = { reminderSet = !reminderSet }
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = stringResource(id = R.string.snooze_after),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier
                            .weight(1f)
                    )
                    Box(
                        modifier = Modifier
                            .clickable { expanded = true }
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "" + remindBeforeValue + " " + stringResource(id = R.string.minutes).lowercase(),
                                style = MaterialTheme.typography.labelMedium,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Icon(
                                imageVector = FAIcons.KeyboardArrowDown,
                                contentDescription = null
                            )
                        }
                        DropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false },
                            modifier = Modifier.background(MaterialTheme.colorScheme.tertiaryContainer)
                        ) {
                            (1..6).forEach { index ->
                                DropDownItem(
                                    item = index * 5,
                                    onItemSelected = { remindBeforeValue = index * 5 },
                                    onDropDownExpanded = { expanded = false },
                                    text = "" + (index * 5) + " " + stringResource(id = R.string.minutes).lowercase(),
                                    color = Color.Transparent
                                )
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.height(24.dp))
                DialogDoneOrCancel(
                    onDismissRequest = { setShowDialog() },
                    value = remindBeforeValue,
                    onSave = {
                        setSnoozeReminder(reminderSet)
                        onSnoozeReminderAtSet(it)
                    }
                )
            }
        }
    }
}

@ThemePreviews
@Composable
fun SnoozeReminderDialogPreview() {
    FocusedAppTheme {
        SnoozeReminderDialog(
            snoozeReminderAt = 5,
            isSnoozeReminderSet = false,
            onSnoozeReminderAtSet = {},
            setShowDialog = {},
            setSnoozeReminder = {}
        )
    }
}