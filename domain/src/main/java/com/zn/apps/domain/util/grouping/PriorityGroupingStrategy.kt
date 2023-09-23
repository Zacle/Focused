package com.zn.apps.domain.util.grouping

import android.content.Context
import com.zn.apps.model.R
import com.zn.apps.model.data.task.TaskPriority
import com.zn.apps.model.data.task.TaskResource

class PriorityGroupingStrategy(
    private val context: Context,
    private val taskResources: List<TaskResource>
): GroupingStrategy {
    override fun group(): Map<String, List<TaskResource>> {
        val priorityGroup = mutableMapOf<String, MutableList<TaskResource>>()
        taskResources.forEach { taskResource ->
            val priority = getPriorityName(context, taskResource.task.priority)
            priorityGroup.getOrPut(priority) { mutableListOf() }.add(taskResource)
        }
        return priorityGroup
    }
}

fun getPriorityName(context: Context, taskPriority: TaskPriority): String =
    when(taskPriority) {
        TaskPriority.HIGH -> context.getString(R.string.high_priority)
        TaskPriority.MEDIUM -> context.getString(R.string.medium_priority)
        TaskPriority.LOW -> context.getString(R.string.low_priority)
        TaskPriority.NONE -> context.getString(R.string.no_priority)
    }