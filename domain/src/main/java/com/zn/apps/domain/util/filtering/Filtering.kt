package com.zn.apps.domain.util.filtering

import com.zn.apps.filter.Filter
import com.zn.apps.filter.Filter.DateFilter
import com.zn.apps.filter.Filter.ProjectFilter
import com.zn.apps.filter.Filter.TagFilter
import com.zn.apps.model.data.task.TaskResource

internal interface Filtering {
    /**
     * Returns the list containing [TaskResource] matching only the given [Filter]
     */
    fun filter(filter: Filter): List<TaskResource>

    companion object {
        /**
         * Given the [Filter] return the strategy that will be used to filter tasks
         *
         * @param filter the type of [Filter] to be applied on tasks
         * @param taskResources the tasks that will be filtered
         */
        fun obtainStrategy(filter: Filter, taskResources: List<TaskResource>): Filtering {
            return when(filter) {
                is TagFilter -> TagFilteringStrategy(taskResources)
                is DateFilter -> DueDateFilteringStrategy(taskResources)
                is ProjectFilter -> ProjectFilteringStrategy(taskResources)
            }
        }
    }
}