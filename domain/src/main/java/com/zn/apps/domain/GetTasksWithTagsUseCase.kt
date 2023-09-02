package com.zn.apps.domain

import com.zn.apps.domain.tag.GetTagsUseCase
import com.zn.apps.domain.task.GetTasksUseCase
import com.zn.apps.filter.Filter
import com.zn.apps.model.data.tag.Tag
import com.zn.apps.model.data.task.RelatedTasksMetaDataResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

class GetTasksWithTagsUseCase(
    configuration: Configuration,
    private val getTasksUseCase: GetTasksUseCase,
    private val getTagsUseCase: GetTagsUseCase
): UseCase<GetTasksWithTagsUseCase.Request, GetTasksWithTagsUseCase.Response>(configuration) {

    override suspend fun process(request: Request): Flow<Response> =
        combine(
            getTasksUseCase.process(
                GetTasksUseCase.Request(
                    filter = request.filter,
                    taskCompleted = request.taskCompleted,
                    projectCompleted = true
                )
            ),
            getTagsUseCase.process(GetTagsUseCase.Request)
        ) { tasksResponse, tagsResponse ->
            Response(
                relatedTasksGrouped = tasksResponse.relatedTasksGrouped,
                tags = tagsResponse.tags
            )
        }

    /**
     * Request parameter to filter the task and group them
     *
     * @param filter how to filter and group tasks
     * @param taskCompleted filter tasks based on completion. Remove tasks that are completed or not
     */
    data class Request(
        val filter: Filter,
        val taskCompleted: Boolean
    ): UseCase.Request

    data class Response(
        val relatedTasksGrouped: Map<String, RelatedTasksMetaDataResult>,
        val tags: List<Tag>
    ): UseCase.Response
}