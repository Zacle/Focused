package com.zn.apps.domain.util.filtering

import com.zn.apps.filter.Filter
import com.zn.apps.filter.Filter.ProjectFilter
import com.zn.apps.model.data.task.TaskResource

class ProjectFilteringStrategy(
    private val taskResources: List<TaskResource>
): Filtering {

    override fun filter(filter: Filter): List<TaskResource> = taskResources.filter {
        it.projectId == (filter as ProjectFilter).projectId
    }
}