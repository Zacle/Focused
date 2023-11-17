package com.zn.apps.ui_design.component.dialog

import android.content.Context
import android.text.format.DateFormat
import android.widget.Toast
import androidx.compose.animation.animateColor
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DatePickerFormatter
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TimePickerDefaults
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.zn.apps.common.DeadlineTimeHelper
import com.zn.apps.common.DeadlineType
import com.zn.apps.ui_design.R
import com.zn.apps.ui_design.component.FocusedAppBackground
import com.zn.apps.ui_design.component.ThemePreviews
import com.zn.apps.ui_design.icon.FAIcons
import com.zn.apps.ui_design.theme.FocusedAppTheme
import java.time.Instant
import java.time.LocalDate
import java.time.LocalTime
import java.time.OffsetDateTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.util.Locale

private val DatePickerTitlePadding = PaddingValues(start = 24.dp, end = 12.dp, top = 16.dp)
private val DatePickerHeadlinePadding = PaddingValues(start = 24.dp, end = 12.dp, bottom = 12.dp)

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun DeadlineSelectionDialog(
    dateTime: OffsetDateTime? = null,
    remindBeforeValue: Int,
    isReminderSet: Boolean,
    onDateTimeWithReminderSet: (OffsetDateTime?, Int, Boolean) -> Unit,
    onDismissRequest: (Boolean) -> Unit
) {
    var date by remember {
        mutableStateOf(dateTime?.toLocalDate())
    }
    var time by remember {
        mutableStateOf(dateTime?.toLocalTime() ?: LocalTime.of(8, 0))
    }
    var timeType: DeadlineType? by remember {
        mutableStateOf(DeadlineTimeHelper.convertToDeadlineType(dateTime))
    }
    var reminderTimeValue by remember {
        mutableIntStateOf(remindBeforeValue)
    }
    var shouldRemind by remember {
        mutableStateOf(isReminderSet)
    }
    var showTimeDialog by remember {
        mutableStateOf(false)
    }
    var showReminderDialog by remember {
        mutableStateOf(false)
    }
    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = dateTime?.toInstant()?.toEpochMilli()
    )
    val timerPickerState = rememberTimePickerState(
        is24Hour = DateFormat.is24HourFormat(LocalContext.current),
        initialHour = time.hour,
        initialMinute = time.minute
    )
    
    if (showTimeDialog) {
        TimePickerDialog(
            onCancel = { showTimeDialog = false },
            onConfirm = {
                time = LocalTime.of(timerPickerState.hour, timerPickerState.minute)
                showTimeDialog = false
            }
        ) {
            TimePicker(
                state = timerPickerState,
                colors = TimePickerDefaults.colors(
                    clockDialColor = MaterialTheme.colorScheme.tertiaryContainer,
                    timeSelectorSelectedContainerColor = MaterialTheme.colorScheme.inversePrimary,
                    timeSelectorUnselectedContainerColor = MaterialTheme.colorScheme.tertiaryContainer
                )
            )
        }
    }

    if (showReminderDialog) {
        ReminderDialog(
            remindBefore = reminderTimeValue,
            isReminderSet = shouldRemind,
            onReminderTimeSet = { reminderTimeValue = it },
            setReminder = { shouldRemind = it },
            setShowDialog = { showReminderDialog = false }
        )
    }

    DatePickerDialog(
        onDismissRequest = { onDismissRequest(false) },
        properties = DialogProperties(
            dismissOnBackPress = true,
            dismissOnClickOutside = false
        ),
        confirmButton = {},
        shape = RoundedCornerShape(20.dp)
    ) {

        Column(
            modifier = Modifier.verticalScroll(rememberScrollState())
        ) {

            DatePicker(
                state = datePickerState,
                showModeToggle = false,
                modifier = Modifier
                    .fillMaxWidth(),
                title = {
                    Text(
                        text = stringResource(id = R.string.select_due_date),
                        modifier = Modifier
                            .padding(DatePickerTitlePadding)
                    )
                },
                headline = {
                    if (datePickerState.selectedDateMillis == null) {
                        Text(
                            text = stringResource(id = R.string.no_date),
                            style = MaterialTheme.typography.headlineMedium,
                            maxLines = 1,
                            modifier = Modifier
                                .padding(DatePickerHeadlinePadding)
                        )
                    } else {
                        DatePickerDefaults.DatePickerHeadline(
                            state = datePickerState,
                            dateFormatter = remember { DatePickerFormatter() },
                            modifier = Modifier
                                .padding(DatePickerHeadlinePadding)
                        )
                    }
                }
            )

            FlowRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 30.dp, vertical = 8.dp)
            ) {
                /**
                 * Due date: Someday
                 */
                DefaultDueDate(
                    selected = timeType == DeadlineType.SOMEDAY,
                    onSelected = {
                        date = null
                        timeType = DeadlineType.SOMEDAY
                        shouldRemind = false
                        datePickerState.setSelection(null)
                    },
                    text = stringResource(id = R.string.someday)
                )
                /**
                 * Due date: Today
                 */
                DefaultDueDate(
                    selected = timeType == DeadlineType.TODAY,
                    onSelected = {
                        date = LocalDate.now()
                        timeType = DeadlineType.TODAY
                        shouldRemind = isReminderSet
                        datePickerState.setSelection(
                            date?.atStartOfDay()?.toInstant(ZoneOffset.UTC)?.toEpochMilli()
                        )
                    },
                    text = stringResource(id = R.string.today)
                )
                /**
                 * Due date: Tomorrow
                 */
                DefaultDueDate(
                    selected = timeType == DeadlineType.TOMORROW,
                    onSelected = {
                        date = LocalDate.now().plusDays(1)
                        timeType = DeadlineType.TOMORROW
                        shouldRemind = isReminderSet
                        datePickerState.setSelection(
                            date?.atStartOfDay()?.toInstant(ZoneOffset.UTC)?.toEpochMilli()
                        )
                    },
                    text = stringResource(id = R.string.tomorrow)
                )
                /**
                 * Due date: Upcoming
                 */
                DefaultDueDate(
                    selected = timeType == DeadlineType.UPCOMING,
                    onSelected = {
                        date = LocalDate.now().plusDays(7)
                        timeType = DeadlineType.UPCOMING
                        shouldRemind = isReminderSet
                        datePickerState.setSelection(
                            date?.atStartOfDay()?.toInstant(ZoneOffset.UTC)?.toEpochMilli()
                        )
                    },
                    text = stringResource(id = R.string.next_week)
                )
            }
            ChooseTaskTimeAndReminder(
                time = time,
                showTimeDialog = { showTimeDialog = true },
                showReminderDialog = { showReminderDialog = true },
                isDateSet = date != null,
                reminderTime = time.minusMinutes(reminderTimeValue.toLong())
            )
            Spacer(modifier = Modifier.height(16.dp))

            DialogDoneOrCancel(
                onDismissRequest = { onDismissRequest(false) },
                value = datePickerState.selectedDateMillis?.let {
                    OffsetDateTime.of(
                        LocalDate.ofInstant(Instant.ofEpochMilli(it), ZoneId.systemDefault()),
                        time,
                        ZoneOffset.systemDefault().rules.getOffset(Instant.now())
                    )
                },
                onSave = {
                    onDateTimeWithReminderSet(it, reminderTimeValue, shouldRemind)
                }
            )
        }
    }
}

@Composable
fun ChooseTaskTimeAndReminder(
    time: LocalTime,
    showTimeDialog: () -> Unit,
    showReminderDialog: () -> Unit,
    modifier: Modifier = Modifier,
    reminderTime: LocalTime? = null,
    isDateSet: Boolean = false
) {
    Column {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = 30.dp, vertical = 4.dp)
                .clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() }
                ) { showTimeDialog() },
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                modifier = Modifier
                    .weight(1.0f),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(id = FAIcons.add_time),
                    contentDescription = stringResource(id = R.string.time),
                    tint = MaterialTheme.colorScheme.secondary.copy(alpha = 0.5f),
                    modifier = Modifier.size(20.dp)
                )
                Text(
                    text = stringResource(id = R.string.time),
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onSurface,
                    style = MaterialTheme.typography.bodyLarge
                )
            }
            Row {
                Text(
                    text = getLocaleTime(LocalContext.current, time),
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    textAlign = TextAlign.End
                )
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        val warningText = stringResource(id = R.string.set_date_first)
        val context = LocalContext.current
        Row(
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = 30.dp, vertical = 4.dp)
                .clickable(
                    enabled = isDateSet,
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() }
                ) {
                    if (isDateSet)
                        showReminderDialog()
                    else
                        Toast.makeText(context, warningText, Toast.LENGTH_SHORT).show()
                },
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                modifier = Modifier
                    .weight(1.0f),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(id = FAIcons.notification),
                    contentDescription = stringResource(id = R.string.reminder),
                    tint = MaterialTheme.colorScheme.secondary.copy(alpha = 0.5f),
                    modifier = Modifier.size(20.dp)
                )
                Text(
                    text = stringResource(id = R.string.reminder),
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onSurface,
                    style = MaterialTheme.typography.bodyLarge
                )
            }
            Row {
                Text(
                    text = reminderTime?.let { getLocaleTime(LocalContext.current, it) } ?: stringResource(
                        id = R.string.no_time
                    ),
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    textAlign = TextAlign.End
                )
            }
        }
    }
}

@ThemePreviews
@Composable
fun ChooseTaskTimeAndReminderPreview() {
    FocusedAppTheme {
        FocusedAppBackground {
            val substract = 5
            ChooseTaskTimeAndReminder(
                time = LocalTime.now(),
                showTimeDialog = {},
                showReminderDialog = {},
                reminderTime = LocalTime.now().minusMinutes(substract.toLong())
            )
        }
    }
}

/**
 * Return the Locale time in a 12-hour or 24-hour depending on the settings
 */
fun getLocaleTime(context: Context, time: LocalTime): String {
    return if (DateFormat.is24HourFormat(context)) {
        time.format(DateTimeFormatter.ofPattern("HH:mm", Locale.US))
    } else {
        time.format(DateTimeFormatter.ofPattern("hh:mm a", Locale.US))
    }
}

@Composable
fun TimePickerDialog(
    onCancel: () -> Unit,
    onConfirm: () -> Unit,
    content: @Composable () -> Unit,
) {
    Dialog(
        onDismissRequest = onCancel,
        properties = DialogProperties(
            usePlatformDefaultWidth = false
        ),
    ) {
        Surface(
            shape = MaterialTheme.shapes.extraLarge,
            tonalElevation = 6.dp,
            modifier = Modifier
                .width(IntrinsicSize.Min)
                .height(IntrinsicSize.Min)
                .background(
                    shape = MaterialTheme.shapes.extraLarge,
                    color = MaterialTheme.colorScheme.surface
                ),
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                content()
                Row(
                    modifier = Modifier
                        .height(40.dp)
                        .fillMaxWidth()
                ) {
                    Spacer(modifier = Modifier.weight(1f))
                    TextButton(
                        onClick = onCancel
                    ) {
                        Text(stringResource(id = R.string.cancel))
                    }
                    TextButton(
                        onClick = onConfirm
                    ) {
                        Text(stringResource(id = R.string.done))
                    }
                }
            }
        }
    }
}

@Composable
fun DefaultDueDate(
    selected: Boolean,
    onSelected: () -> Unit,
    text: String,
    modifier: Modifier = Modifier
) {

    val transition = updateTransition(targetState = selected, label = "pomodoro tab transition")

    val backgroundColor by transition.animateColor(
        label = "default due date background transition"
    ) {
        if (it) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)
    }

    val contentColor by transition.animateColor(
        label = "default due date content color transition"
    ) {
        if (it) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant
    }

    Surface(
        modifier = modifier
            .padding(end = 4.dp, bottom = 8.dp),
        color = backgroundColor,
        contentColor = contentColor,
        shape = MaterialTheme.shapes.medium
    ) {

        val interactionSource = remember { MutableInteractionSource() }

        val pressed by interactionSource.collectIsPressedAsState()
        val backgroundPressed =
            if (pressed)
                Modifier.background(MaterialTheme.colorScheme.primary)
            else
                Modifier.background(Color.Transparent)

        Box(modifier = Modifier
            .toggleable(
                value = selected,
                onValueChange = { onSelected() },
                interactionSource = interactionSource,
                indication = null
            )
            .then(backgroundPressed)
        ) {
            Text(
                text = text,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
            )
        }
    }
}

@ThemePreviews
@Composable
fun DeadlineSelectionDialogPreview() {
    FocusedAppTheme {
        DeadlineSelectionDialog(
            remindBeforeValue = 5,
            isReminderSet = true,
            onDateTimeWithReminderSet = { _, _, _ -> },
            onDismissRequest = {},
            dateTime = OffsetDateTime.now().plusDays(2)
        )
    }
}

@Preview(device = Devices.NEXUS_5)
@Composable
fun DeadlineSelectionDialogPhonePreview() {
    FocusedAppTheme {
        DeadlineSelectionDialog(
            remindBeforeValue = 5,
            isReminderSet = true,
            onDateTimeWithReminderSet = { _, _, _ -> },
            onDismissRequest = {}
        )
    }
}