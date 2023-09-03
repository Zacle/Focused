package com.zn.apps.focused.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.zn.apps.feature.onboarding.navigation.onboardingScreen
import com.zn.apps.focused.navigation.Graph.MAIN_GRAPH_ROUTE
import com.zn.apps.focused.navigation.Graph.ROOT_GRAPH_ROUTE
import com.zn.apps.focused.navigation.graphs.mainGraph
import com.zn.apps.focused.ui.FocusedAppState

@Composable
fun FANavHost(
    startDestination: String,
    appState: FocusedAppState,
    modifier: Modifier
) {
    val navController: NavHostController = appState.navController

    NavHost(
        navController = navController,
        route = ROOT_GRAPH_ROUTE,
        startDestination = startDestination,
        modifier = modifier
    ) {
        onboardingScreen {
            navController.popBackStack()
            navController.navigate(MAIN_GRAPH_ROUTE)
        }
        mainGraph(appState = appState)
    }
}

object Graph {
    const val ROOT_GRAPH_ROUTE = "root_graph"
    const val MAIN_GRAPH_ROUTE = "main_graph"
}