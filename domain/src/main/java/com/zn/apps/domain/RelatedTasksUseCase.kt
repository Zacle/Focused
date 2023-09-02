package com.zn.apps.domain

import com.zn.apps.common.DeadlineTimeHelper
import com.zn.apps.model.data.task.RelatedTasksMetaDataResult
import com.zn.apps.model.data.task.TaskResource

/**
 * Helper class that takes the list of grouped tasks and returns the
 * meta data of those tasks to bed displayed on the screen
 */

class RelatedTasksUseCase {
    operator fun invoke(taskResources: List<TaskResource>): RelatedTasksMetaDataResult {
        var totalEstimatedTime: Long = 0
        var totalElapsedTime: Long = 0

        taskResources.forEach { taskResource ->
            totalEstimatedTime += taskResource.task.pomodoro.getEstimatedRemainingTime()
            totalElapsedTime += taskResource.task.pomodoro.elapsedTime
        }
        return RelatedTasksMetaDataResult(
            estimatedTime = DeadlineTimeHelper.getTaskTime(totalEstimatedTime),
            tasksToBeCompleted = taskResources.size,
            elapsedTime = DeadlineTimeHelper.getTaskTime(totalElapsedTime),
            tasks = taskResources
        )
    }
}