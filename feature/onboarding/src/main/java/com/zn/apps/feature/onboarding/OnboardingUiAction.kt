package com.zn.apps.feature.onboarding

import com.zn.apps.ui_common.state.UiAction

sealed class OnboardingUiAction: UiAction {
    data object Load: OnboardingUiAction()
    data object SetOnboarded: OnboardingUiAction()
}