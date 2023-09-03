package com.zn.apps.focused.ui.screenspecs

import androidx.annotation.StringRes
import com.zn.apps.ui_design.icon.Icon

sealed interface BottomNavScreenSpec: ScreenSpec {

    companion object {
        val screens: List<BottomNavScreenSpec> = ScreenSpec
            .allScreens
            .values
            .filterIsInstance<BottomNavScreenSpec>()
    }

    val icon: Icon

    @get:StringRes
    val label: Int
}