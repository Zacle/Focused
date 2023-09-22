package com.zn.apps.domain.project

import com.zn.apps.domain.UseCase
import com.zn.apps.domain.repository.ProjectRepository
import com.zn.apps.domain.task.TasksFilteringGroupingFacade
import com.zn.apps.filter.Filter
import com.zn.apps.model.data.task.RelatedTasksMetaDataResult
import com.zn.apps.model.data.task.TaskResource
import com.zn.apps.model.usecase.UseCaseException
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.mapLatest

@OptIn(ExperimentalCoroutinesApi::class)
class GetPopulatedProjectResourceUseCase(
    configuration: Configuration,
    private val projectRepository: ProjectRepository
): UseCase<GetPopulatedProjectResourceUseCase.Request, GetPopulatedProjectResourceUseCase.Response>(configuration) {

    override suspend fun process(request: Request): Flow<Response> {
        val (projectId, isTaskCompleted, filter) = request
        return projectRepository.getPopulatedProjectResource(projectId).mapLatest {
            if (it == null) {
                throw UseCaseException.ProjectNotFoundException(Throwable())
            } else {
                val tasksFilteringGroupingFacade = TasksFilteringGroupingFacade(
                    taskResources = filterTasks(it.taskResources, isTaskCompleted),
                    filter = filter,
                    filterCompletedProject = false
                )
                Response(tasksFilteringGroupingFacade.execute())
            }
        }
    }

    private fun filterTasks(taskResources: List<TaskResource>, completed: Boolean) =
        taskResources.filter { it.task.completed == completed }

    data class Request(
        val projectId: String,
        val isTaskCompleted: Boolean,
        val filter: Filter
    ): UseCase.Request

    data class Response(val relatedTasksGrouped: Map<String, RelatedTasksMetaDataResult>): UseCase.Response
}