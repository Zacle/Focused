package com.zn.apps.domain.project

import com.zn.apps.common.DeadlineTimeHelper
import com.zn.apps.common.MetadataResult
import com.zn.apps.common.MetadataType
import com.zn.apps.domain.UseCase
import com.zn.apps.domain.repository.ProjectRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetProjectMetadataUseCase(
    configuration: Configuration,
    private val projectRepository: ProjectRepository
): UseCase<GetProjectMetadataUseCase.Request, GetProjectMetadataUseCase.Response>(configuration) {

    override suspend fun process(request: Request): Flow<Response> {
        return projectRepository.getPopulatedProjectResource(request.projectId).map { populatedResource ->
            if (populatedResource == null) {
                Response(MetadataResult.initialMetadataResult)
            } else {
                var totalTime = 0L
                var estimatedTime = 0L
                var elapsedTime = 0L
                var tasksToBeCompleted = 0
                var tasksCompleted = 0
                populatedResource.taskResources.forEach { taskResource ->
                    val pomodoro = taskResource.task.pomodoro
                    totalTime += pomodoro.pomodoroNumber * pomodoro.pomodoroLength
                    estimatedTime +=
                        if (!taskResource.task.completed) pomodoro.getEstimatedRemainingTime() else 0
                    elapsedTime += pomodoro.elapsedTime
                    if (taskResource.task.completed)
                        tasksCompleted += 1
                    else
                        tasksToBeCompleted += 1
                }
                val result = MetadataResult(
                    totalTaskTime = DeadlineTimeHelper.getTaskTime(totalTime),
                    estimatedTaskTime = DeadlineTimeHelper.getTaskTime(estimatedTime),
                    elapsedTaskTime = DeadlineTimeHelper.getTaskTime(elapsedTime),
                    totalTime = totalTime,
                    estimatedTime = estimatedTime,
                    elapsedTime = elapsedTime,
                    totalTasks = populatedResource.taskResources.size,
                    tasksToBeCompleted = tasksToBeCompleted,
                    tasksCompleted = tasksCompleted,
                    metadataType = MetadataType.SHOW
                )
                Response(result)
            }
        }
    }

    data class Request(val projectId: String): UseCase.Request

    data class Response(val metadataResult: MetadataResult): UseCase.Response
}