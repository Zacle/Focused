package com.zn.apps.focused.ui

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination
import com.zn.apps.focused.ui.navigation.FANavHost
import com.zn.apps.focused.ui.navigation.Graph
import com.zn.apps.feature.onboarding.navigation.onboardingRoute
import com.zn.apps.focused.ui.screenspecs.BottomNavScreenSpec
import com.zn.apps.ui_design.component.FANavigationBar
import com.zn.apps.ui_design.component.FANavigationBarItem
import com.zn.apps.ui_design.component.FocusedAppBackground
import com.zn.apps.ui_design.component.FocusedAppGradientBackground
import com.zn.apps.ui_design.icon.Icon.DrawableResourceIcon
import com.zn.apps.ui_design.icon.Icon.ImageVectorIcon
import com.zn.apps.ui_design.theme.GradientColors
import com.zn.apps.ui_design.theme.LocalGradientColors

@Composable
fun FocusedApp(
    shouldHideOnboarding: Boolean,
    appState: FocusedAppState = rememberFocusedAppState()
) {

    val shouldShowGradientBackground = appState.shouldShowBottomBar

    FocusedAppBackground {
        FocusedAppGradientBackground(
            gradientColors = if (shouldShowGradientBackground) {
                LocalGradientColors.current
            } else {
                GradientColors()
            }
        ) {
            ModalNavigationDrawer(
                drawerState = appState.drawerState,
                gesturesEnabled = appState.shouldShowBottomBar,
                drawerContent = { /*TODO*/ }
            ) {
                MainScaffold(
                    appState = appState,
                    shouldHideOnboarding = shouldHideOnboarding
                )
            }
        }
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MainScaffold(
    appState: FocusedAppState,
    shouldHideOnboarding: Boolean
) {
    Scaffold(
        containerColor = Color.Transparent,
        contentColor = MaterialTheme.colorScheme.onBackground,
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        snackbarHost = { SnackbarHost(appState.snackbarHostState) },
        bottomBar = {
            if (appState.shouldShowBottomBar) {
                FABottomBar(
                    destinations = appState.topLevelDestinations,
                    onNavigateToDestination = appState::navigateToTopLevelDestination,
                    currentDestination = appState.currentDestination,
                    modifier = Modifier
                )
            }
        }
    ) { padding ->
        val startDestination =
            if (shouldHideOnboarding)
                Graph.MAIN_GRAPH_ROUTE
            else
                onboardingRoute
        FANavHost(
            appState = appState,
            startDestination = startDestination,
            modifier = Modifier.padding(padding)
        )
    }
}

@Composable
fun FABottomBar(
    destinations: List<BottomNavScreenSpec>,
    onNavigateToDestination: (String) -> Unit,
    currentDestination: NavDestination?,
    modifier: Modifier
) {
   FANavigationBar(
       modifier = modifier
   ) {
       destinations.forEach { destination ->
           val selected = currentDestination?.route == destination.route
           FANavigationBarItem(
               selected = selected,
               onClick = { onNavigateToDestination(destination.route) },
               icon = { 
                   when(destination.icon) {
                       is ImageVectorIcon -> Icon(
                           imageVector = (destination.icon as ImageVectorIcon).imageVector,
                           contentDescription = null,
                           modifier = Modifier.size(24.dp)
                       )
                       is DrawableResourceIcon -> Icon(
                           painter = painterResource(id = (destination.icon as DrawableResourceIcon).id),
                           contentDescription = null,
                           modifier = Modifier.size(24.dp)
                       )
                   }
               },
               label = { Text(text = stringResource(id = destination.label)) }
           )
       }
   } 
}