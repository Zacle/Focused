package com.zn.apps.focused.ui

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination
import com.zn.apps.feature.onboarding.navigation.onboardingRoute
import com.zn.apps.focused.R
import com.zn.apps.focused.ui.navigation.FANavHost
import com.zn.apps.focused.ui.navigation.Graph
import com.zn.apps.focused.ui.screenspecs.BottomNavScreenSpec
import com.zn.apps.focused.ui.screenspecs.NavigationDrawerScreenSpec
import com.zn.apps.ui_design.component.FANavigationBar
import com.zn.apps.ui_design.component.FANavigationBarItem
import com.zn.apps.ui_design.component.FANavigationDrawerItem
import com.zn.apps.ui_design.component.FocusedAppBackground
import com.zn.apps.ui_design.component.FocusedAppGradientBackground
import com.zn.apps.ui_design.component.ThemePreviews
import com.zn.apps.ui_design.icon.Icon.DrawableResourceIcon
import com.zn.apps.ui_design.icon.Icon.ImageVectorIcon
import com.zn.apps.ui_design.theme.FocusedAppTheme
import com.zn.apps.ui_design.theme.GradientColors
import com.zn.apps.ui_design.theme.LocalGradientColors
import kotlinx.coroutines.launch

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
                drawerContent = {
                    ModalDrawerSheet(
                        drawerContainerColor = MaterialTheme.colorScheme.background,
                        drawerContentColor = MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier
                            .requiredWidth(320.dp)
                    ) {
                        FANavigationDrawerHeader(
                            modifier = Modifier
                                .height(200.dp)
                        )
                        Spacer(modifier = Modifier.size(16.dp))
                        FANavigationDrawer(
                            appState = appState,
                            destinations = appState.navigationDrawerDestinations,
                            currentDestination = appState.currentDestination
                        )
                    }
                }
            ) {
                MainScaffold(
                    appState = appState,
                    shouldHideOnboarding = shouldHideOnboarding
                )
            }
        }
    }
}

@Composable
fun FANavigationDrawerHeader(
    modifier: Modifier = Modifier
) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        Image(
            painter = painterResource(id = R.drawable.splash),
            contentDescription = null,
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = stringResource(id = R.string.app_name),
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Medium
        )
        Spacer(modifier = Modifier.height(12.dp))
        Divider(
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}

@Composable
fun FANavigationDrawer(
    appState: FocusedAppState,
    destinations: List<NavigationDrawerScreenSpec>,
    currentDestination: NavDestination?
) {
    destinations.forEach { destination ->
        val selected = currentDestination?.route == destination.route
        FANavigationDrawerItem(
            label = {
                Text(
                    text = stringResource(destination.label),
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )
            },
            selected = selected,
            onClick = {
                appState.coroutineScope.launch {
                    appState.drawerState.close()
                }
                appState.navController.navigate(destination.route)
            },
            icon = {
                when(destination.icon) {
                    is DrawableResourceIcon -> {
                        Icon(
                            painter = painterResource(id = (destination.icon as DrawableResourceIcon).id),
                            contentDescription = null,
                            modifier = Modifier.size(24.dp),
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                    is ImageVectorIcon -> {
                        Icon(
                            imageVector = (destination.icon as ImageVectorIcon).imageVector,
                            contentDescription = null,
                            modifier = Modifier.size(24.dp),
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }
        )
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
        bottomBar = {
            if (appState.shouldShowBottomBar) {
                FABottomBar(
                    destinations = appState.topLevelDestinations,
                    onNavigateToDestination = appState::navigateToTopLevelDestination,
                    currentDestination = appState.currentDestination,
                    modifier = Modifier
                )
            }
        },
        snackbarHost = { SnackbarHost(hostState = appState.snackbarHostState) }
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

@ThemePreviews
@Composable
fun FANavigationDrawerHeaderPreview() {
    FocusedAppTheme {
        FocusedAppBackground {
            FANavigationDrawerHeader()
        }
    }
}