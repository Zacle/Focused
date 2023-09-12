package com.zn.apps.ui_design.component.dialog

import androidx.compose.animation.animateColor
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.chargemap.compose.numberpicker.NumberPicker
import com.zn.apps.ui_design.R
import com.zn.apps.ui_design.component.ThemePreviews
import com.zn.apps.ui_design.theme.FocusedAppTheme

@Composable
fun PomodoroDialog(
    pomodoroNumber: Int,
    pomodoroLength: Int,
    setShowDialog: (Boolean) -> Unit,
    onSave: (UiPomodoroState) -> Unit
) {

    var selectedPomodoroTab by remember {
        mutableStateOf(SelectedPomodoroTab.NUMBER)
    }

    var pomodoroNumberValue by remember {
        mutableIntStateOf(pomodoroNumber)
    }
    var pomodoroLengthValue by remember {
        mutableIntStateOf(pomodoroLength)
    }

    Dialog(
        onDismissRequest = { setShowDialog(false) },
        properties = DialogProperties(
            dismissOnClickOutside = false
        )
    ) {
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            color = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.onSurface
        ) {
            Column(
                modifier = Modifier.padding(horizontal = 4.dp, vertical = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    PomodoroTab(
                        modifier = Modifier.weight(1.0f),
                        text = stringResource(id = R.string.pomodoro_number),
                        selected = selectedPomodoroTab == SelectedPomodoroTab.NUMBER
                    ) {
                        selectedPomodoroTab = SelectedPomodoroTab.NUMBER
                    }
                    Spacer(
                        modifier = Modifier.size(2.dp)
                    )
                    PomodoroTab(
                        modifier = Modifier.weight(1.0f),
                        text = stringResource(id = R.string.pomodoro_length),
                        selected = selectedPomodoroTab == SelectedPomodoroTab.LENGTH
                    ) {
                        selectedPomodoroTab = SelectedPomodoroTab.LENGTH
                    }
                }
                Spacer(
                    modifier = Modifier
                        .fillMaxWidth()
                        .size(16.dp)
                )
                val estimatedTime = stringResource(id = R.string.estimated_task_time) +
                        " = ${getEstimatedTime(pomodoroNumberValue, pomodoroLengthValue)}"
                Text(
                    text = estimatedTime,
                    style = MaterialTheme.typography.bodySmall,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(
                    modifier = Modifier
                        .fillMaxWidth()
                        .size(8.dp)
                )
                when(selectedPomodoroTab) {
                    SelectedPomodoroTab.NUMBER -> {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            NumberPicker(
                                value = pomodoroNumberValue,
                                onValueChange = {
                                    pomodoroNumberValue = it
                                },
                                range = 0..100,
                                dividersColor = Color.Transparent,
                                textStyle = TextStyle(
                                    color = MaterialTheme.colorScheme.onBackground,
                                    fontStyle = MaterialTheme.typography.bodySmall.fontStyle
                                )
                            )
                            Spacer(modifier = Modifier.width(16.dp))
                            Text(
                                text = stringResource(id = R.string.pomodoro),
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
                    SelectedPomodoroTab.LENGTH -> {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            NumberPicker(
                                value = pomodoroLengthValue,
                                onValueChange = {
                                    pomodoroLengthValue = it
                                },
                                range = 1..500,
                                dividersColor = Color.Transparent,
                                textStyle = TextStyle(
                                    color = MaterialTheme.colorScheme.onBackground,
                                    fontStyle = MaterialTheme.typography.bodySmall.fontStyle
                                )
                            )
                            Spacer(modifier = Modifier.width(16.dp))
                            Text(
                                text = stringResource(id = R.string.minutes),
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
                }
                DialogDoneOrCancel(
                    onDismissRequest = setShowDialog,
                    value = UiPomodoroState(
                        pomodoroNumber = pomodoroNumberValue,
                        pomodoroLength = pomodoroLengthValue
                    ),
                    onSave = onSave
                )
            }
        }
    }
}

fun getEstimatedTime(
    pomodoroNumber: Int,
    pomodoroLength: Int
): String {
    val totalHours = pomodoroNumber * pomodoroLength
    val hours = totalHours / 60
    val minutes = totalHours % 60
    var estimatedTime = ""
    if (hours > 0) estimatedTime += "${hours}h"
    if (minutes > 0) estimatedTime += " ${minutes}m"
    return estimatedTime.ifEmpty { "0m" }
}

@Composable
fun PomodoroTab(
    text: String,
    selected: Boolean,
    modifier: Modifier = Modifier,
    onSelected: () -> Unit,
) {

    val transition = updateTransition(targetState = selected, label = "pomodoro tab transition")

    val background by transition.animateColor(
        label = "pomodoroTabBackground",
        transitionSpec = { spring(dampingRatio = Spring.DampingRatioMediumBouncy) }
    ) {
        if (it) MaterialTheme.colorScheme.primary else Color.Transparent
    }
    val contentColor by transition.animateColor(
        label = "pomodoroTabContentColor",
        transitionSpec = { tween(durationMillis = 300, easing = LinearOutSlowInEasing) }
    ) {
        if (it) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface
    }
    val borderColor by transition.animateColor(
        label = "pomodoroTabBorderColor",
        transitionSpec = { tween(durationMillis = 300, easing = FastOutLinearInEasing) }
    ) {
        if (it) Color.Transparent else MaterialTheme.colorScheme.primary
    }
    val shape = RoundedCornerShape(percent = 25)

    Box(
        modifier = modifier
            .clip(shape)
            .background(background)
            .border(
                width = 1.dp,
                color = borderColor,
                shape = shape
            )
            .selectable(selected, onClick = onSelected),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.titleSmall,
            color = contentColor,
            modifier = Modifier.padding(2.dp)
        )
    }
}

enum class SelectedPomodoroTab {
    NUMBER,
    LENGTH
}

/**
 * State Holder class for the Pomodoro object and dialog
 */
@Stable
data class UiPomodoroState(
    val pomodoroNumber: Int = 0,
    val pomodoroLength: Int = 25
)


@ThemePreviews
@Composable
fun PomodoroNumberPreview() {
    FocusedAppTheme {
        PomodoroTab(text = "Pomodoro Number", selected = false) {

        }
    }
}

@ThemePreviews
@Composable
fun PomodoroNumberSelectedPreview() {
    FocusedAppTheme {
        PomodoroTab(text = "Pomodoro Number", selected = true) {

        }
    }
}