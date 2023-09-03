package com.zn.apps.feature.onboarding.data

import androidx.annotation.StringRes
import com.zn.apps.feature.onboarding.R
import com.zn.apps.ui_design.icon.Icon.DrawableResourceIcon

/**
 * Onboarding data used to introduces the main functionalities of Focused App to first-time users
 */
sealed class OnboardingPage(
    val image: DrawableResourceIcon,
    @StringRes val title: Int,
    @StringRes val description: Int
) {

    data object OrganizeTasks: OnboardingPage(
        image = DrawableResourceIcon(R.drawable.organize_tasks),
        title = R.string.organize_tasks_title,
        description = R.string.organize_tasks_description
    )

    data object RunTasks: OnboardingPage(
        image = DrawableResourceIcon(R.drawable.run_tasks),
        title = R.string.run_tasks_title,
        description = R.string.run_tasks_description
    )

    data object ScheduleTasks: OnboardingPage(
        image = DrawableResourceIcon(R.drawable.schedule_tasks),
        title = R.string.schedule_tasks_title,
        description = R.string.schedule_tasks_description
    )
}