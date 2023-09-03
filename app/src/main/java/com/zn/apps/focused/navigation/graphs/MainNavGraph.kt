package com.zn.apps.focused.navigation.graphs

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.zn.apps.focused.navigation.Graph.MAIN_GRAPH_ROUTE
import com.zn.apps.focused.ui.FocusedAppState
import com.zn.apps.focused.ui.screenspecs.BottomNavScreenSpec
import com.zn.apps.focused.ui.screenspecs.TasksScreenSpec

fun NavGraphBuilder.mainGraph(
    appState: FocusedAppState
) {
    navigation(
        route = MAIN_GRAPH_ROUTE,
        startDestination = TasksScreenSpec.route
    ) {
        BottomNavScreenSpec.screens.forEach { screen ->
            composable(
                route = screen.route,
                arguments = screen.arguments,
                deepLinks = screen.deepLinks
            ) {
                screen.Content(
                    appState = appState,
                    navBackStackEntry = it
                )
            }
        }
    }
}