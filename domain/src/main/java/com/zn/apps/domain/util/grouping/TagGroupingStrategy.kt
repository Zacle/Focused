package com.zn.apps.domain.util.grouping

import com.zn.apps.model.data.task.TaskResource

class TagGroupingStrategy(
    private val taskResources: List<TaskResource>
): GroupingStrategy {
    override fun group(): Map<String, List<TaskResource>> {
        val tagGroup = mutableMapOf<String, MutableList<TaskResource>>()
        taskResources.forEach { taskResource ->
            tagGroup.getOrPut(taskResource.tagName) { mutableListOf() }.add(taskResource)
        }
        return tagGroup
    }
}