package com.zn.apps.domain.task

import android.content.Context
import com.zn.apps.domain.UseCase
import com.zn.apps.domain.repository.TaskRepository
import com.zn.apps.filter.Filter
import com.zn.apps.model.data.task.RelatedTasksMetaDataResult
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.mapLatest

@OptIn(ExperimentalCoroutinesApi::class)
class GetTasksUseCase(
    configuration: Configuration,
    private val context: Context,
    private val taskRepository: TaskRepository
): UseCase<GetTasksUseCase.Request, GetTasksUseCase.Response>(configuration) {

    override suspend fun process(request: Request): Flow<Response> {
        val (filter, taskCompleted, projectCompleted) = request
        return taskRepository.getTaskResources(taskCompleted).mapLatest {
            val tasksFilteringGroupingFacade = TasksFilteringGroupingFacade(
                context = context,
                taskResources = it,
                filter = filter,
                filterCompletedProject = projectCompleted
            )
            Response(tasksFilteringGroupingFacade.execute())
        }
    }

    data class Request(
        val filter: Filter,
        val taskCompleted: Boolean,
        val projectCompleted: Boolean
    ): UseCase.Request

    data class Response(val relatedTasksGrouped: Map<String, RelatedTasksMetaDataResult>): UseCase.Response
}