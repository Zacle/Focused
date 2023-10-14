package com.zn.apps.focused.ui.screenspecs

import androidx.compose.runtime.Composable
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavDeepLink
import com.zn.apps.focused.ui.FocusedAppState

sealed interface ScreenSpec {

    companion object {
        val allScreens = listOf(
            TimerScreenSpec,
            TasksScreenSpec,
            ProjectsScreenSpec,
            ReportScreenSpec,
            TaskScreenSpec,
            TagsScreenSpec,
            CompletedTasksScreenSpec,
            SettingsScreenSpec
        ).associateBy { it.route }
    }

    /* screen route */
    val route: String

    /* screen arguments */
    val arguments: List<NamedNavArgument> get() = emptyList()

    /* deeplink to access the screen from external apps */
    val deepLinks: List<NavDeepLink> get() = emptyList()

    @Composable
    fun Content(appState: FocusedAppState, navBackStackEntry: NavBackStackEntry)
}