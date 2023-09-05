package com.zn.apps.ui_design.util

import androidx.compose.ui.graphics.Color
import com.zn.apps.model.data.task.TaskPriority

/**
 * The color priorities can have to distinguish them
 */
sealed class PriorityColor {

    data class High(val color: Color = Color.Red): PriorityColor()
    data class Medium(val color: Color = Color.Yellow): PriorityColor()
    data class Low(val color: Color = Color.Green): PriorityColor()
    data class None(val color: Color = Color.Gray): PriorityColor()

}

fun getPriorityColor(taskPriority: TaskPriority) =
    when(taskPriority) {
        TaskPriority.HIGH -> PriorityColor.High().color
        TaskPriority.MEDIUM -> PriorityColor.Medium().color
        TaskPriority.LOW -> PriorityColor.Low().color
        TaskPriority.NONE -> PriorityColor.None().color
    }