package com.zn.apps.focused.ui.screenspecs

import androidx.annotation.StringRes
import com.zn.apps.ui_design.icon.Icon

sealed interface NavigationDrawerScreenSpec: ScreenSpec {

    companion object {
        val screens: List<NavigationDrawerScreenSpec> = ScreenSpec
            .allScreens
            .values
            .filterIsInstance<NavigationDrawerScreenSpec>()
    }

    val icon: Icon

    @get:StringRes
    val label: Int
}