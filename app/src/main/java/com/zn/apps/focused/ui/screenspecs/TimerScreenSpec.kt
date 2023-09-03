package com.zn.apps.focused.ui.screenspecs

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavBackStackEntry
import com.zn.apps.feature.timer.R
import com.zn.apps.focused.ui.FocusedAppState
import com.zn.apps.ui_design.icon.FAIcons
import com.zn.apps.ui_design.icon.Icon
import com.zn.apps.ui_design.icon.Icon.DrawableResourceIcon

data object TimerScreenSpec: BottomNavScreenSpec {

    override val icon: Icon = DrawableResourceIcon(FAIcons.TimerDestination)

    override val label: Int = R.string.timer

    override val route: String = "timer"

    @Composable
    override fun TopBar(appState: FocusedAppState, navBackStackEntry: NavBackStackEntry) {
        TODO("Not yet implemented")
    }

    @Composable
    override fun Content(appState: FocusedAppState, navBackStackEntry: NavBackStackEntry) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(text = "Timer")
        }
    }
}