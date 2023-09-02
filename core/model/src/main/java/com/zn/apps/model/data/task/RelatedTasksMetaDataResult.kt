package com.zn.apps.model.data.task

/**
 * Class used to group related tasks and their metadata.
 * Related tasks can be tasks that can be grouped in the same:
 *  - [DeadlineGrouping]
 *  - [DateGrouping]
 *  - [PriorityGrouping]
 *  - [ProjectGrouping]
 *  - [TagGrouping]
 *
 *  Metadata include:
 *  @param estimatedTime the estimated time left to complete the remaining tasks in the group
 *  @param tasksToBeCompleted number of tasks left to be completed
 *  @param elapsedTime the time spent on the tasks in the group
 */
data class RelatedTasksMetaDataResult(
    val estimatedTime: TaskTime,
    val tasksToBeCompleted: Int,
    val elapsedTime: TaskTime,
    val tasks: List<TaskResource>
)
