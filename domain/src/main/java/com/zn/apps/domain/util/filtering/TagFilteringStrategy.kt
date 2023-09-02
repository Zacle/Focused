package com.zn.apps.domain.util.filtering

import com.zn.apps.filter.Filter
import com.zn.apps.filter.Filter.TagFilter
import com.zn.apps.model.data.task.TaskResource

internal class TagFilteringStrategy(
    private val taskResources: List<TaskResource>
): Filtering {
    override fun filter(filter: Filter): List<TaskResource> {
        val filterId = (filter as TagFilter).filterId
        return if (filterId.isEmpty()) {
            taskResources
        } else {
            taskResources.filter {
                it.tagId == filter.filterId
            }
        }
    }
}