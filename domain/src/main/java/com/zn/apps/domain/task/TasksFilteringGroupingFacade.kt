package com.zn.apps.domain.task

import android.content.Context
import com.zn.apps.domain.RelatedTasksUseCase
import com.zn.apps.domain.util.filtering.Filtering
import com.zn.apps.domain.util.grouping.GroupingStrategy
import com.zn.apps.filter.Filter
import com.zn.apps.model.data.task.RelatedTasksMetaDataResult
import com.zn.apps.model.data.task.TaskResource

internal class TasksFilteringGroupingFacade(
    private val context: Context,
    private val taskResources: List<TaskResource>,
    private val filter: Filter,
    private val filterCompletedProject: Boolean
) {

    fun execute(): Map<String, RelatedTasksMetaDataResult> {
        val filterCompletedProject = taskResources.filterCompletedProject(filterCompletedProject)
        val filteringStrategy = Filtering.obtainStrategy(filter, filterCompletedProject)
        val groupingStrategy = GroupingStrategy.obtainStrategy(
            context = context,
            grouping = filter.grouping,
            taskResources = filteringStrategy.filter(filter)
        )
        val groupedTaskResources = groupingStrategy.group()
        return computeGroupsMetadata(groupedTaskResources)
    }

    private fun computeGroupsMetadata(
        groupedTaskResources: Map<String, List<TaskResource>>
    ): Map<String, RelatedTasksMetaDataResult> {
        val relatedTaskResourcesGroupMetadata = mutableMapOf<String, RelatedTasksMetaDataResult>()
        groupedTaskResources.forEach { (key, value) ->
            relatedTaskResourcesGroupMetadata[key] = RelatedTasksUseCase()(value)
        }
        return relatedTaskResourcesGroupMetadata
    }
}

/**
 * Filter completed project first. We cannot display tasks belonging to a project that is
 * already completed. Those tasks will be displayed only in the project screen
 */
fun List<TaskResource>.filterCompletedProject(completed: Boolean): List<TaskResource> {
    return if (completed) {
        this.filter { taskResource ->
            !taskResource.projectCompleted
        }
    } else {
        this
    }
}