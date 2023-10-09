package com.zn.apps.focused.ui.screenspecs

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.material3.DrawerValue
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.zn.apps.feature.timer.R
import com.zn.apps.feature.timer.ui.TIMER_TASK_ID_ARGUMENT
import com.zn.apps.feature.timer.ui.TimerRoute
import com.zn.apps.feature.timer.ui.TimerUiAction
import com.zn.apps.feature.timer.ui.TimerViewModel
import com.zn.apps.focused.ui.FocusedAppState
import com.zn.apps.model.data.pomodoro.PomodoroState
import com.zn.apps.ui_design.icon.FAIcons
import com.zn.apps.ui_design.icon.Icon
import com.zn.apps.ui_design.icon.Icon.DrawableResourceIcon
import kotlinx.coroutines.launch

data object TimerScreenSpec: BottomNavScreenSpec {

    override val icon: Icon = DrawableResourceIcon(FAIcons.TimerDestination)

    override val label: Int = R.string.timer

    override val route: String = "timer?$TIMER_TASK_ID_ARGUMENT={$TIMER_TASK_ID_ARGUMENT}"

    override val arguments: List<NamedNavArgument>
        get() = listOf(
            navArgument(TIMER_TASK_ID_ARGUMENT) {
                type = NavType.StringType
                nullable = true
            }
        )

    fun buildRoute(taskId: String) = "timer?$TIMER_TASK_ID_ARGUMENT=$taskId"

    @Composable
    override fun Content(appState: FocusedAppState, navBackStackEntry: NavBackStackEntry) {
        val viewModel: TimerViewModel = hiltViewModel()

        LaunchedEffect(Unit) {
            viewModel.submitAction(TimerUiAction.Load)
        }

        val uiStateHolder by viewModel.uiStateHolder.collectAsStateWithLifecycle()
        val pomodoroState by viewModel.pomodoroState.collectAsStateWithLifecycle(
            initialValue = PomodoroState.initialState
        )

        TimerRoute(
            pomodoroState = pomodoroState,
            uiStateHolder = uiStateHolder,
            onStartTimer = { viewModel.submitAction(TimerUiAction.StartTimer) },
            onPauseTimer = { viewModel.submitAction(TimerUiAction.PauseTimer) },
            onCompleteTask = { task -> viewModel.submitAction(TimerUiAction.CompleteTask(task)) },
            onStopTimerPressed = { viewModel.submitAction(TimerUiAction.StopTimerPressed) },
            onStopTimerDismissed = { viewModel.submitAction(TimerUiAction.StopTimerDismissed) },
            onStopTimerConfirmed = { viewModel.submitAction(TimerUiAction.StopTimerConfirmed) },
            onSkipBreakPressed = { viewModel.submitAction(TimerUiAction.SkipBreakPressed) },
            onSkipBreakDismissed = { viewModel.submitAction(TimerUiAction.SkipBreakDismissed) },
            onSkipBreakConfirmed = { viewModel.submitAction(TimerUiAction.SkipBreakConfirmed) },
            onDrawerPressed = {
                appState.coroutineScope.launch {
                    appState.drawerState.animateTo(
                        targetValue = DrawerValue.Open,
                        anim = tween(600, easing = FastOutSlowInEasing)
                    )
                }
            }
        )
    }
}