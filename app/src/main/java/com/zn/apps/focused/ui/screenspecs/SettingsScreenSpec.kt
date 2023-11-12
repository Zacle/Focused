package com.zn.apps.focused.ui.screenspecs

import androidx.compose.runtime.Composable
import androidx.navigation.NavBackStackEntry
import com.zn.apps.feature.settings.R
import com.zn.apps.feature.settings.SettingsRoute
import com.zn.apps.focused.ui.FocusedAppState
import com.zn.apps.ui_design.icon.FAIcons
import com.zn.apps.ui_design.icon.Icon

data object SettingsScreenSpec: NavigationDrawerScreenSpec {
    
    override val icon: Icon = Icon.DrawableResourceIcon(FAIcons.settings)
    
    override val label: Int = R.string.settings
    
    override val route: String = "settings"

    @Composable
    override fun Content(appState: FocusedAppState, navBackStackEntry: NavBackStackEntry) {
        SettingsRoute(
            onUpPressed = { appState.navController.navigateUp() },
            onPomodoroSettingsPressed = {
                appState.navigateToPomodoroSettings()
            },
            onCustomizeSettingsPressed = {},
            onSoundNotificationPressed = {
                appState.navigateToSoundNotificationSettings()
            }
        )
    }
}