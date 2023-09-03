package com.zn.apps.focused

import com.zn.apps.ui_common.state.UiAction

sealed class MainActivityUiAction: UiAction {
    data object Load: MainActivityUiAction()
}
