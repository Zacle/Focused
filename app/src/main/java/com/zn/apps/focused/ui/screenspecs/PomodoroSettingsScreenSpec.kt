package com.zn.apps.focused.ui.screenspecs

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.zn.apps.feature.settings.pomodoro.PomodoroSettingsRoute
import com.zn.apps.feature.settings.pomodoro.PomodoroSettingsUiAction
import com.zn.apps.feature.settings.pomodoro.PomodoroSettingsViewModel
import com.zn.apps.focused.ui.FocusedAppState
import com.zn.apps.ui_common.state.CommonScreen

data object PomodoroSettingsScreenSpec: ScreenSpec {

    override val route: String = "settings/pomodoro"

    @Composable
    override fun Content(appState: FocusedAppState, navBackStackEntry: NavBackStackEntry) {
        val viewModel: PomodoroSettingsViewModel = hiltViewModel()

        LaunchedEffect(Unit) {
            viewModel.submitAction(PomodoroSettingsUiAction.Load)
        }

        viewModel.uiStateFlow.collectAsStateWithLifecycle().value.let { state ->
            CommonScreen(state = state) { pomodoroSettingsUiModel ->
                PomodoroSettingsRoute(
                    pomodoroSettingsUiModel = pomodoroSettingsUiModel,
                    onUpPressed = { appState.navController.navigateUp() },
                    onSetPomodoroLength = {
                        viewModel.submitAction(PomodoroSettingsUiAction.SetPomodoroLength(it))
                    },
                    onSetLongBreakLength = {
                        viewModel.submitAction(PomodoroSettingsUiAction.SetLongBreakLength(it))
                    },
                    onSetShortBreakLength = {
                        viewModel.submitAction(PomodoroSettingsUiAction.SetShortBreakLength(it))
                    },
                    onSetLongBreakAfter = {
                        viewModel.submitAction(PomodoroSettingsUiAction.SetLongBreakAfter(it))
                    },
                    onDisableBreak = {
                        viewModel.submitAction(PomodoroSettingsUiAction.SetDisableBreak(it))
                    },
                    onSetAutoStartNextPomodoro = {
                        viewModel.submitAction(PomodoroSettingsUiAction.SetAutoStartNextPomodoro(it))
                    },
                    onSetAutoStartBreak = {
                        viewModel.submitAction(PomodoroSettingsUiAction.SetAutoStartBreak(it))
                    }
                )
            }
        }
    }
}

fun NavGraphBuilder.pomodoroSettingsScreen(appState: FocusedAppState) {
    composable(
        route = PomodoroSettingsScreenSpec.route
    ) {
        PomodoroSettingsScreenSpec.Content(
            appState = appState,
            navBackStackEntry = it
        )
    }
}