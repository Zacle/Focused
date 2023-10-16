package com.zn.apps.feature.timer.ui

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ContentTransform
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.zn.apps.common.millisecondsToHours
import com.zn.apps.common.millisecondsToMinutes
import com.zn.apps.common.millisecondsToSeconds
import com.zn.apps.feature.timer.R
import com.zn.apps.model.data.pomodoro.PomodoroState
import com.zn.apps.model.data.pomodoro.TimerState
import com.zn.apps.model.data.task.Task
import com.zn.apps.ui_design.component.FATopAppBar
import com.zn.apps.ui_design.component.FocusedAppGradientBackground
import com.zn.apps.ui_design.component.TaskCard
import com.zn.apps.ui_design.component.ThemePreviews
import com.zn.apps.ui_design.component.dialog.SimpleConfirmationDialog
import com.zn.apps.ui_design.icon.FAIcons
import com.zn.apps.ui_design.icon.Icon
import com.zn.apps.ui_design.theme.FocusedAppTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimerRoute(
    pomodoroState: PomodoroState,
    uiStateHolder: TimerUiStateHolder,
    onStartTimer: () -> Unit,
    onPauseTimer: () -> Unit,
    onCompleteTask: (Task) -> Unit,
    onStopTimerPressed: () -> Unit,
    onStopTimerDismissed: () -> Unit,
    onStopTimerConfirmed: () -> Unit,
    onSkipBreakPressed: () -> Unit,
    onSkipBreakDismissed: () -> Unit,
    onSkipBreakConfirmed: () -> Unit,
    onDrawerPressed: () -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        containerColor = Color.Transparent,
        contentColor = MaterialTheme.colorScheme.onBackground,
        topBar = {
            FATopAppBar(
                titleName = stringResource(id = R.string.timer),
                navigationIcon = Icon.DrawableResourceIcon(FAIcons.menu),
                onNavigationIconClicked = onDrawerPressed
            ) {
                Spacer(modifier = Modifier.height(24.dp))
            }
        },
        modifier = modifier
    ) { innerPadding ->
        TimerScreen(
            pomodoroState = pomodoroState,
            uiStateHolder = uiStateHolder,
            onStartTimer = onStartTimer,
            onPauseTimer = onPauseTimer,
            onCompleteTask = onCompleteTask,
            onStopTimerPressed = onStopTimerPressed,
            onStopTimerDismissed = onStopTimerDismissed,
            onStopTimerConfirmed = onStopTimerConfirmed,
            onSkipBreakPressed = onSkipBreakPressed,
            onSkipBreakDismissed = onSkipBreakDismissed,
            onSkipBreakConfirmed = onSkipBreakConfirmed,
            modifier = Modifier.padding(innerPadding)
        )
    }
}

@Composable
fun TimerScreen(
    pomodoroState: PomodoroState,
    uiStateHolder: TimerUiStateHolder,
    onStartTimer: () -> Unit,
    onPauseTimer: () -> Unit,
    onCompleteTask: (Task) -> Unit,
    onStopTimerPressed: () -> Unit,
    onStopTimerDismissed: () -> Unit,
    onStopTimerConfirmed: () -> Unit,
    onSkipBreakPressed: () -> Unit,
    onSkipBreakDismissed: () -> Unit,
    onSkipBreakConfirmed: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(24.dp))
        Timer(pomodoroState = pomodoroState)
        if (pomodoroState.taskIdRunning.isNotEmpty() && uiStateHolder.task != null) {
            Spacer(modifier = Modifier.size(16.dp))
            RunningTask(
                pomodoroState = pomodoroState,
                task = uiStateHolder.task,
                onCompleteTask = onCompleteTask
            )
        }
        Spacer(modifier = Modifier.size(16.dp))
        TimerStateButton(
            pomodoroState = pomodoroState,
            onStartTimer = onStartTimer,
            onPauseTimer = onPauseTimer,
            onStopTimerPressed = onStopTimerPressed,
            onSkipBreakPressed = onSkipBreakPressed
        )
    }

    if (uiStateHolder.showStopTimerDialog) {
        SimpleConfirmationDialog(
            title = R.string.stop_pomodoro,
            text = R.string.stop_pomodoro_text,
            confirmationAction = { onStopTimerConfirmed() },
            cancelAction = { onStopTimerDismissed() }
        )
    }
    if (uiStateHolder.showSkipBreakDialog) {
        SimpleConfirmationDialog(
            title = R.string.skip_break_title,
            text = R.string.skip_break_text,
            confirmationAction = { onSkipBreakConfirmed() },
            cancelAction = { onSkipBreakDismissed() }
        )
    }
}

@Composable
fun RunningTask(
    task: Task,
    onCompleteTask: (Task) -> Unit,
    pomodoroState: PomodoroState?
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(
            text = stringResource(id = R.string.running_task),
            style = MaterialTheme.typography.labelMedium
        )
        Spacer(modifier = Modifier.size(16.dp))
        TaskCard(
            task = task,
            navigateToTask = {},
            onCompleted = { onCompleteTask(task) },
            isTaskRunning = task.id == pomodoroState?.taskIdRunning,
            onCollapse = {},
            onExpand = {},
            cardOffset = 0f,
            onStartTask = {}
        )
    }
}

@Composable
fun TimerStateButton(
    pomodoroState: PomodoroState,
    onStartTimer: () -> Unit,
    onPauseTimer: () -> Unit,
    onStopTimerPressed: () -> Unit,
    onSkipBreakPressed: () -> Unit
) {
    Crossfade(
        targetState = pomodoroState.timerState,
        label = "Timer state button"
    ) {
        when(it) {
            TimerState.IDLE -> {
                IdleTimerButton(
                    isBreak = pomodoroState.currentPhase.isBreak,
                    onStartTimer = onStartTimer
                )
            }
            TimerState.PAUSED -> {
                RunningTimerButton(
                    text = stringResource(id = R.string.resume),
                    primaryAction = { onStartTimer() },
                    cancelAction = { onStopTimerPressed() }
                )
            }
            TimerState.RUNNING -> {
                if (pomodoroState.currentPhase.isBreak) {
                    TimerButton(
                        text = stringResource(id = R.string.skip_break),
                        color = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary,
                        action = { onSkipBreakPressed() }
                    )
                } else {
                    RunningTimerButton(
                        text = stringResource(id = R.string.pause),
                        primaryAction = { onPauseTimer() },
                        cancelAction = { onStopTimerPressed() }
                    )
                }
            }
        }
    }
}

@Composable
fun RunningTimerButton(
    text: String,
    primaryAction: () -> Unit,
    cancelAction: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        TimerButton(
            text = text,
            color = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary,
            action = { primaryAction() }
        )
        Spacer(modifier = Modifier.size(16.dp))
        TimerButton(
            text = stringResource(id = R.string.stop_timer),
            color = MaterialTheme.colorScheme.error,
            contentColor = MaterialTheme.colorScheme.onError,
            action = { cancelAction() }
        )
    }
}

@Composable
fun TimerButton(
    text: String,
    color: Color,
    contentColor: Color,
    action: () -> Unit
) {
    Box(
        modifier = Modifier
            .clickable { action() }
            .background(
                color = color,
                shape = MaterialTheme.shapes.small
            ),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.bodyMedium,
            color = contentColor,
            modifier = Modifier.padding(horizontal = 24.dp, vertical = 8.dp)
        )
    }
}

@Composable
fun IdleTimerButton(
    isBreak: Boolean,
    onStartTimer: () -> Unit
) {
    val text  =
        if (isBreak)
            stringResource(id = R.string.start_break)
        else
            stringResource(id = R.string.start_timer)
    TimerButton(
        text = text,
        color = MaterialTheme.colorScheme.primary,
        contentColor = MaterialTheme.colorScheme.onPrimary,
        action = { onStartTimer() }
    )
}

@Composable
fun Timer(
    pomodoroState: PomodoroState
) {
    val timeLeftInMillis = pomodoroState.timeLeftInMillis
    val targetTimeInMillis = pomodoroState.targetTimeInMillis

    val progress = timeLeftInMillis.toFloat() / targetTimeInMillis.toFloat()

    CircularProgress(
        timeLeftInMillis = timeLeftInMillis,
        progress = progress
    )
}

@Composable
fun CircularProgress(
    progress: Float,
    modifier: Modifier = Modifier,
    timeLeftInMillis: Long = 0L,
) {
    Box(
        modifier = modifier.size(250.dp),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(
            progress = 1f,
            color = MaterialTheme.colorScheme.onPrimaryContainer.copy(0.1f),
            strokeWidth = 4.dp,
            modifier = modifier
                .fillMaxSize()
                .scale(scaleX = -1f, scaleY = 1f)
        )
        CircularProgressIndicator(
            progress = progress,
            color = MaterialTheme.colorScheme.primary,
            strokeWidth = 4.dp,
            modifier = modifier
                .fillMaxSize()
                .scale(scaleX = -1f, scaleY = 1f)
        )
        DisplayCountDown(
            hours = timeLeftInMillis.millisecondsToHours(),
            minutes = timeLeftInMillis.millisecondsToMinutes(),
            seconds = timeLeftInMillis.millisecondsToSeconds()
        )
    }
}

@Composable
fun DisplayCountDown(
    hours: Int,
    minutes: Int,
    seconds: Int
) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        AnimatedContent(
            targetState = hours,
            transitionSpec = { addAnimation() },
            label = "Display count down hour"
        ) { updatedHour ->
            Text(
                text = String.format("%02d", updatedHour),
                style = TextStyle(
                    fontSize = MaterialTheme.typography.displayMedium.fontSize,
                    fontWeight = FontWeight.Bold,
                    fontStyle = MaterialTheme.typography.displayMedium.fontStyle
                )
            )
        }
        Spacer(modifier = Modifier.size(2.dp))
        Text(
            text = ":",
            style = TextStyle(
                fontSize = MaterialTheme.typography.displayMedium.fontSize,
                fontWeight = FontWeight.Bold,
                fontStyle = MaterialTheme.typography.displayMedium.fontStyle,
                textAlign = TextAlign.Center
            )
        )
        Spacer(modifier = Modifier.size(2.dp))
        AnimatedContent(
            targetState = minutes,
            transitionSpec = { addAnimation() },
            label = "Display count down minute"
        ) { updatedMinute ->
            Text(
                text = String.format("%02d", updatedMinute),
                style = TextStyle(
                    fontSize = MaterialTheme.typography.displayMedium.fontSize,
                    fontWeight = FontWeight.Bold,
                    fontStyle = MaterialTheme.typography.displayMedium.fontStyle
                )
            )
        }
        Spacer(modifier = Modifier.size(2.dp))
        Text(
            text = ":",
            style = TextStyle(
                fontSize = MaterialTheme.typography.displayMedium.fontSize,
                fontWeight = FontWeight.Bold,
                fontStyle = MaterialTheme.typography.displayMedium.fontStyle,
                textAlign = TextAlign.Center
            )
        )
        Spacer(modifier = Modifier.size(2.dp))
        AnimatedContent(
            targetState = seconds,
            transitionSpec = { addAnimation() },
            label = "Display count down second"
        ) { updatedSecond ->
            Text(
                text = String.format("%02d", updatedSecond),
                style = TextStyle(
                    fontSize = MaterialTheme.typography.displayMedium.fontSize,
                    fontWeight = FontWeight.Bold,
                    fontStyle = MaterialTheme.typography.displayMedium.fontStyle
                )
            )
        }
    }
}

fun addAnimation(duration: Int = 200): ContentTransform {
    return (slideInVertically(animationSpec = tween(durationMillis = duration)) { height -> height } + fadeIn(
        animationSpec = tween(durationMillis = duration)
    )).togetherWith(slideOutVertically(animationSpec = tween(durationMillis = duration)) { height -> height } + fadeOut(
        animationSpec = tween(durationMillis = duration)
    ))
}

@ThemePreviews
@Composable
fun DisplayCountDownPreview() {
    FocusedAppTheme {
        FocusedAppGradientBackground {
            DisplayCountDown(hours = 2, minutes = 32, seconds = 45)
        }
    }
}

@ThemePreviews
@Composable
fun CircularProgressPreview() {
    FocusedAppTheme {
        FocusedAppGradientBackground {
            CircularProgress(
                timeLeftInMillis = 15230146,
                progress = 0.6f
            )
        }
    }
}

@ThemePreviews
@Composable
fun TimerButtonPreview() {
    FocusedAppTheme {
        FocusedAppGradientBackground {
            TimerButton(
                text = stringResource(id = R.string.start_timer),
                color = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ) {

            }
        }
    }
}