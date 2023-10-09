package com.zn.apps.focused.ui

import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.NavDestination
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.zn.apps.focused.ui.screenspecs.BottomNavScreenSpec
import com.zn.apps.focused.ui.screenspecs.NavigationDrawerScreenSpec
import kotlinx.coroutines.CoroutineScope

@Composable
fun rememberFocusedAppState(
    navController: NavHostController = rememberNavController(),
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() },
    drawerState: DrawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
) = remember(
    navController,
    coroutineScope,
    snackbarHostState,
    drawerState
) {
    FocusedAppState(
        navController,
        coroutineScope,
        snackbarHostState,
        drawerState
    )
}

@Stable
class FocusedAppState(
    val navController: NavHostController,
    val coroutineScope: CoroutineScope,
    val snackbarHostState: SnackbarHostState,
    val drawerState: DrawerState
) {

    /**
     * BottomBar state source of truth to be referenced in the app
     */
    val topLevelDestinations = BottomNavScreenSpec.screens
    private val bottomBarRoutes = topLevelDestinations.map { it.route }

    /**
     * Navigation Drawer destinations
     */
    val navigationDrawerDestinations = NavigationDrawerScreenSpec.screens

    val currentDestination: NavDestination?
        @Composable get() = navController
            .currentBackStackEntryAsState().value?.destination

    val currentRoute: String?
        get() = navController.currentDestination?.route

    val shouldShowBottomBar: Boolean
        @Composable get() =
            currentDestination?.route in bottomBarRoutes

    /**
     * UI logic for navigating to a top level destination(bottom bars) in the app.
     * Top level destinations have only one copy of the destination of the back stack,
     * and save and restore state whenever you navigate to and from it.
     *
     * @param route: The destination the app needs to navigate to.
     */
    fun navigateToTopLevelDestination(route: String) {
        if (route != currentRoute) {
            navController.navigate(route) {
                // Pop up to the start destination of the graph to
                // avoid building up a large stack of destinations
                // on the back stack as users select items
                popUpTo(navController.graph.findStartDestination().id) {
                    saveState = true
                }
                // Avoid multiple copies of the same destination when
                // reselecting the same item
                launchSingleTop = true
                // Restore state when re-selecting a previously selected item
                restoreState = true
            }
        }
    }
}