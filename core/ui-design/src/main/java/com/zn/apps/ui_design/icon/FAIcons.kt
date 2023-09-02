package com.zn.apps.ui_design.icon

import androidx.annotation.DrawableRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.ui.graphics.vector.ImageVector
import com.zn.apps.ui_design.R

object FAIcons {
    val ProjectDestination = R.drawable.project
    val ReportDestination = R.drawable.report
    val TaskDestination = R.drawable.task
    val TimerDestination = R.drawable.timer
    val ArrowForward = Icons.Default.ArrowForward
    val KeyboardArrowRight = Icons.Default.KeyboardArrowRight
    val pomodoro = R.drawable.pomodoro
    val play_circle = R.drawable.play_circle
    val calendar = R.drawable.calendar
    val add_time = R.drawable.time_add
    val delete = R.drawable.delete
}

/**
 * A sealed class to make dealing with [ImageVector] and [DrawableRes] icons easier.
 */
sealed class Icon {
    data class ImageVectorIcon(val imageVector: ImageVector) : Icon()
    data class DrawableResourceIcon(@DrawableRes val id: Int) : Icon()
}