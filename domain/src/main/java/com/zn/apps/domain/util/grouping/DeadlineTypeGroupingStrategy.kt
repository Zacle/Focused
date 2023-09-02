package com.zn.apps.domain.util.grouping

import com.zn.apps.common.DeadlineTimeHelper
import com.zn.apps.common.DeadlineType
import com.zn.apps.model.data.task.TaskResource

class DeadlineTypeGroupingStrategy(
    private val taskResources: List<TaskResource>
): GroupingStrategy {
    /**
     * Group tasks based on due date. Due dates are first converted to [DeadlineType] and then
     * tasks with the same type are put in the same group
     */
    override fun group(): Map<String, List<TaskResource>> {
        val dateGroup = mutableMapOf<String, MutableList<TaskResource>>()
        taskResources.forEach { taskResource ->
            val date = DeadlineTimeHelper.convertToDeadlineType(taskResource.task.dueDate).name
            dateGroup.getOrPut(date) { mutableListOf() }.add(taskResource)
        }
        return dateGroup.toSortedMap(compareBy { DeadlineType.valueOf(it) })
    }
}