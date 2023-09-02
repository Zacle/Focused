package com.zn.apps.domain.util.grouping

import com.zn.apps.model.data.task.TaskResource

class PriorityGroupingStrategy(
    private val taskResources: List<TaskResource>
): GroupingStrategy {
    override fun group(): Map<String, List<TaskResource>> {
        val priorityGroup = mutableMapOf<String, MutableList<TaskResource>>()
        taskResources.forEach { taskResource ->
            val priority = taskResource.task.priority.name
            priorityGroup.getOrPut(priority) { mutableListOf() }.add(taskResource)
        }
        return priorityGroup
    }
}