package com.zn.apps.feature.settings.pomodoro

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.chargemap.compose.numberpicker.NumberPicker
import com.zn.apps.feature.settings.R
import com.zn.apps.feature.settings.SettingsTextField
import com.zn.apps.feature.settings.SettingsTextFieldSwitch
import com.zn.apps.model.datastore.PomodoroPreferences
import com.zn.apps.ui_design.component.FATopAppBar
import com.zn.apps.ui_design.component.dialog.DialogDoneOrCancel
import com.zn.apps.ui_design.icon.FAIcons
import com.zn.apps.ui_design.icon.Icon

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PomodoroSettingsRoute(
    pomodoroSettingsUiModel: PomodoroSettingsUiModel,
    onUpPressed: () -> Unit,
    onSetPomodoroLength: (Int) -> Unit,
    onSetLongBreakLength: (Int) -> Unit,
    onSetShortBreakLength: (Int) -> Unit,
    onSetLongBreakAfter: (Int) -> Unit,
    onDisableBreak: (Boolean) -> Unit,
    onSetAutoStartNextPomodoro: (Boolean) -> Unit,
    onSetAutoStartBreak: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        topBar = {
            FATopAppBar(
                titleName = stringResource(id = R.string.pomodoro_settings),
                navigationIcon = Icon.ImageVectorIcon(FAIcons.ArrowBack),
                onNavigationIconClicked = onUpPressed
            ) {}
        }
    ) { innerPadding ->
        PomodoroSettingsScreen(
            pomodoroPreferences = pomodoroSettingsUiModel.pomodoroPreferences,
            onSetPomodoroLength = onSetPomodoroLength,
            onSetLongBreakLength = onSetLongBreakLength,
            onSetShortBreakLength = onSetShortBreakLength,
            onSetLongBreakAfter = onSetLongBreakAfter,
            onDisableBreak = onDisableBreak,
            onSetAutoStartNextPomodoro = onSetAutoStartNextPomodoro,
            onSetAutoStartBreak = onSetAutoStartBreak,
            modifier = modifier
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
fun PomodoroSettingsScreen(
    pomodoroPreferences: PomodoroPreferences,
    onSetPomodoroLength: (Int) -> Unit,
    onSetLongBreakLength: (Int) -> Unit,
    onSetShortBreakLength: (Int) -> Unit,
    onSetLongBreakAfter: (Int) -> Unit,
    onDisableBreak: (Boolean) -> Unit,
    onSetAutoStartNextPomodoro: (Boolean) -> Unit,
    onSetAutoStartBreak: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    val (uiState, uiSateSetter) = remember { mutableStateOf(PomodoroSettingsStateDialog()) }

    Surface(
        modifier = modifier
    ) {
        Column(
            verticalArrangement = Arrangement.Center
        ) {
            SettingsTextField(
                title = stringResource(id = R.string.pomodoro_length),
                selection = "" + pomodoroPreferences.pomodoroLength + " " + pluralStringResource(
                    R.plurals.minutes,
                    pomodoroPreferences.pomodoroLength,
                    pomodoroPreferences.pomodoroLength
                ),
                onClicked = {
                    uiSateSetter(
                        uiState.copy(showPomodoroLengthDialog = true)
                    )
                }
            )
            SettingsTextField(
                title = stringResource(id = R.string.long_break_length),
                selection = "" + pomodoroPreferences.longBreakLength + " " + pluralStringResource(
                    R.plurals.minutes,
                    pomodoroPreferences.longBreakLength,
                    pomodoroPreferences.longBreakLength
                ),
                onClicked = {
                    uiSateSetter(
                        uiState.copy(showLongBreakLengthDialog = true)
                    )
                }
            )
            SettingsTextField(
                title = stringResource(id = R.string.short_break_length),
                selection = "" + pomodoroPreferences.shortBreakLength + " " + pluralStringResource(
                    R.plurals.minutes,
                    pomodoroPreferences.shortBreakLength,
                    pomodoroPreferences.shortBreakLength
                ),
                onClicked = {
                    uiSateSetter(
                        uiState.copy(showShortBreakLengthDialog = true)
                    )
                }
            )
            SettingsTextField(
                title = stringResource(id = R.string.long_break_after),
                selection = "" + pomodoroPreferences.numberOfPomodoroBeforeLongBreak + " " +
                            stringResource(id = R.string.pomodoro),
                onClicked = {
                    uiSateSetter(
                        uiState.copy(showLongBreakAfterDialog = true)
                    )
                }
            )
            SettingsTextFieldSwitch(
                title = stringResource(id = R.string.disable_break),
                checked = pomodoroPreferences.disableBreak,
                onCheckedChanged = {
                    onDisableBreak(it)
                }
            )
            SettingsTextFieldSwitch(
                title = stringResource(id = R.string.auto_start_next_pomodoro),
                checked = pomodoroPreferences.autoStartNextPomodoro,
                onCheckedChanged = {
                    onSetAutoStartNextPomodoro(it)

                }
            )
            SettingsTextFieldSwitch(
                title = stringResource(id = R.string.auto_start_break),
                checked = pomodoroPreferences.autoStartBreak,
                onCheckedChanged = {
                    onSetAutoStartBreak(it)

                }
            )
        }
    }
    if (uiState.showPomodoroLengthDialog) {
        FANumberPicker(
            value = pomodoroPreferences.pomodoroLength,
            startInterval = 1,
            endInterval = 500,
            title = stringResource(id = R.string.pomodoro_length),
            label = stringResource(id = R.string.minute),
            onValueChange = {
                onSetPomodoroLength(it)
            },
            onCancel = {
                uiSateSetter(
                    uiState.copy(showPomodoroLengthDialog = false)
                )
            }
        )
    }
    if (uiState.showLongBreakLengthDialog) {
        FANumberPicker(
            value = pomodoroPreferences.longBreakLength,
            startInterval = 1,
            endInterval = 120,
            title = stringResource(id = R.string.long_break_length),
            label = stringResource(id = R.string.minute),
            onValueChange = {
                onSetLongBreakLength(it)
            },
            onCancel = {
                uiSateSetter(
                    uiState.copy(showLongBreakLengthDialog = false)
                )
            }
        )
    }
    if (uiState.showShortBreakLengthDialog) {
        FANumberPicker(
            value = pomodoroPreferences.shortBreakLength,
            startInterval = 1,
            endInterval = 120,
            title = stringResource(id = R.string.short_break_length),
            label = stringResource(id = R.string.minute),
            onValueChange = {
                onSetShortBreakLength(it)
            },
            onCancel = {
                uiSateSetter(
                    uiState.copy(showShortBreakLengthDialog = false)
                )
            }
        )
    }
    if (uiState.showLongBreakAfterDialog) {
        FANumberPicker(
            value = pomodoroPreferences.numberOfPomodoroBeforeLongBreak,
            startInterval = 1,
            endInterval = 100,
            title = stringResource(id = R.string.long_break_after),
            label = stringResource(id = R.string.pomodoro),
            onValueChange = {
                onSetLongBreakAfter(it)
            },
            onCancel = {
                uiSateSetter(
                    uiState.copy(showLongBreakAfterDialog = false)
                )
            }
        )
    }
}

@Composable
fun FANumberPicker(
    value: Int,
    startInterval: Int,
    endInterval: Int,
    title: String,
    label: String,
    onValueChange: (Int) -> Unit,
    onCancel: () -> Unit,
    modifier: Modifier = Modifier
) {
    var currentValue by remember { mutableIntStateOf(value) }

    Dialog(onDismissRequest = onCancel) {
        Surface(
            modifier = modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            color = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.onSurface
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier
                        .padding(horizontal = 8.dp, vertical = 12.dp)
                )
                Divider()
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    NumberPicker(
                        value = currentValue,
                        onValueChange = { currentValue = it },
                        range = startInterval..endInterval,
                        dividersColor = Color.Transparent,
                        textStyle = TextStyle(
                            color = MaterialTheme.colorScheme.onBackground,
                            fontStyle = MaterialTheme.typography.bodyMedium.fontStyle
                        )
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Text(
                        text = label,
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
                DialogDoneOrCancel(
                    onDismissRequest = { onCancel() },
                    value = currentValue,
                    onSave = onValueChange
                )
            }
        }
    }
}

@Immutable
private data class PomodoroSettingsStateDialog(
    val showPomodoroLengthDialog: Boolean = false,
    val showLongBreakLengthDialog: Boolean = false,
    val showShortBreakLengthDialog: Boolean = false,
    val showLongBreakAfterDialog: Boolean = false
)