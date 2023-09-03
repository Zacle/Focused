package com.zn.apps.feature.onboarding.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.zn.apps.feature.onboarding.OnboardingRoute

const val onboardingRoute = "onboarding"
fun NavGraphBuilder.onboardingScreen(navigateToHome: () -> Unit) {
    composable(
        route = onboardingRoute
    ) {
        OnboardingRoute(navigateToHome = navigateToHome)
    }
}