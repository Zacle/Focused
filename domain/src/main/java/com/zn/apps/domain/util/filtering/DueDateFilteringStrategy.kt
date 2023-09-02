package com.zn.apps.domain.util.filtering

import com.zn.apps.common.DeadlineTimeHelper
import com.zn.apps.filter.Filter
import com.zn.apps.filter.Filter.DateFilter
import com.zn.apps.model.data.task.TaskResource

class DueDateFilteringStrategy (
    private val taskResources: List<TaskResource>
): Filtering {
    override fun filter(filter: Filter): List<TaskResource> = taskResources.filter {
        DeadlineTimeHelper.convertToDeadlineType(it.task.dueDate) == (filter as DateFilter).dueDate
    }
}