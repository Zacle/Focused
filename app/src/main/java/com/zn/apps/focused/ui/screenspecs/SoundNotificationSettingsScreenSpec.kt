package com.zn.apps.focused.ui.screenspecs

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.zn.apps.feature.settings.sound_and_notification.SoundNotificationSettingsRoute
import com.zn.apps.feature.settings.sound_and_notification.SoundNotificationSettingsUiAction
import com.zn.apps.feature.settings.sound_and_notification.SoundNotificationSettingsViewModel
import com.zn.apps.focused.ui.FocusedAppState
import com.zn.apps.ui_common.state.CommonScreen

data object SoundNotificationSettingsScreenSpec: ScreenSpec {

    override val route: String = "settings/sound_notification"

    @Composable
    override fun Content(appState: FocusedAppState, navBackStackEntry: NavBackStackEntry) {
        val viewModel: SoundNotificationSettingsViewModel = hiltViewModel()

        LaunchedEffect(Unit) {
            viewModel.submitAction(SoundNotificationSettingsUiAction.Load)
        }

        viewModel.uiStateFlow.collectAsStateWithLifecycle().value.let { state ->
            CommonScreen(state = state) {soundNotificationSettingsUiModel ->
                SoundNotificationSettingsRoute(
                    soundNotificationSettingsUiModel = soundNotificationSettingsUiModel,
                    onUpPressed = { appState.navController.navigateUp() },
                    onSetTaskReminderMinutes = {
                        viewModel.submitAction(SoundNotificationSettingsUiAction.SetTaskReminderMinutes(it))
                    },
                    onSetDailyTodoReminder = {
                        viewModel.submitAction(SoundNotificationSettingsUiAction.SetDailyTodoReminder(it))
                    },
                    onSetSnoozeTaskReminder = {
                        viewModel.submitAction(SoundNotificationSettingsUiAction.SetSnoozeTaskReminder(it))
                    },
                    onSetSnoozeTaskReminderAfter = {
                        viewModel.submitAction(SoundNotificationSettingsUiAction.SetSnoozeTaskReminderAfter(it))
                    }
                )
            }
        }
    }
}

fun NavGraphBuilder.soundNotificationSettingsScreen(appState: FocusedAppState) {
    composable(
        route = SoundNotificationSettingsScreenSpec.route
    ) {
        SoundNotificationSettingsScreenSpec.Content(
            appState = appState,
            navBackStackEntry = it
        )
    }
}