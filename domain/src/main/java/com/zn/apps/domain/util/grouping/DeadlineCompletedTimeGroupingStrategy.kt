package com.zn.apps.domain.util.grouping

import android.content.Context
import com.zn.apps.common.DeadlineTime
import com.zn.apps.model.data.task.TaskResource

class DeadlineCompletedTimeGroupingStrategy(
    private val context: Context,
    private val taskResources: List<TaskResource>
): GroupingStrategy {
    /**
     * Group tasks based on completed date. Completed dates are first converted to [DeadlineTime] and then
     * tasks with the same time are put in the same group
     */
    override fun group(): Map<String, List<TaskResource>> {
        val dateGroup = mutableMapOf<String, MutableList<TaskResource>>()
        taskResources.sortedByDescending { it.task.completedTime }.forEach { taskResource ->
            val date = getDeadlineTimeName(context, taskResource.task.dueDate)
            dateGroup.getOrPut(date) { mutableListOf() }.add(taskResource)
        }
        return dateGroup
    }
}