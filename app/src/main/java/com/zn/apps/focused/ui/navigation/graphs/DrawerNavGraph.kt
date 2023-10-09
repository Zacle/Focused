package com.zn.apps.focused.ui.navigation.graphs

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.zn.apps.focused.ui.FocusedAppState
import com.zn.apps.focused.ui.navigation.Graph.NAV_DRAWER_GRAPH_ROUTE
import com.zn.apps.focused.ui.screenspecs.NavigationDrawerScreenSpec
import com.zn.apps.focused.ui.screenspecs.TagsScreenSpec

fun NavGraphBuilder.drawerNavGraph(
    appState: FocusedAppState
) {
    navigation(
        route = NAV_DRAWER_GRAPH_ROUTE,
        startDestination = TagsScreenSpec.route
    ) {
        NavigationDrawerScreenSpec.screens.forEach { screen ->
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