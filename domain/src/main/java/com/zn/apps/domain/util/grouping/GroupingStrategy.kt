package com.zn.apps.domain.util.grouping

import com.zn.apps.filter.Grouping
import com.zn.apps.filter.Grouping.DeadlineTimeGrouping
import com.zn.apps.filter.Grouping.DeadlineTypeGrouping
import com.zn.apps.filter.Grouping.PriorityGrouping
import com.zn.apps.filter.Grouping.TagGrouping
import com.zn.apps.model.data.task.TaskResource


interface GroupingStrategy {
    /**
     * Returns a map where each key matches the given [Grouping] and the value
     * its corresponding list of [TaskResource]
     */
    fun group(): Map<String, List<TaskResource>>

    companion object {
        /**
         * Given the [Grouping] return the strategy that will be used to filter tasks
         *
         * @param grouping the type of [Grouping] to be applied on tasks
         * @param taskResources the tasks that will be filtered
         */
        fun obtainStrategy(grouping: Grouping, taskResources: List<TaskResource>): GroupingStrategy {
            return when(grouping) {
                is DeadlineTimeGrouping -> DeadlineTimeGroupingStrategy(taskResources)
                is DeadlineTypeGrouping -> DeadlineTypeGroupingStrategy(taskResources)
                is PriorityGrouping -> PriorityGroupingStrategy(taskResources)
                is TagGrouping -> TagGroupingStrategy(taskResources)
            }
        }
    }
}