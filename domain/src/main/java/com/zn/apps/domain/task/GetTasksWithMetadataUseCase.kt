package com.zn.apps.domain.task

import com.zn.apps.common.MetadataResult
import com.zn.apps.domain.UseCase
import com.zn.apps.filter.Filter
import com.zn.apps.model.data.task.RelatedTasksMetaDataResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

class GetTasksWithMetadataUseCase(
    configuration: Configuration,
    private val getTasksUseCase: GetTasksUseCase,
    private val getTasksMetadataUseCase: GetTasksMetadataUseCase
): UseCase<GetTasksWithMetadataUseCase.Request, GetTasksWithMetadataUseCase.Response>(configuration) {

    override suspend fun process(request: Request): Flow<Response> =
        combine(
            getTasksUseCase.process(
                GetTasksUseCase.Request(
                    filter = request.filter,
                    taskCompleted = request.taskCompleted,
                    projectCompleted = true
                )
            ),
            getTasksMetadataUseCase.process(
                GetTasksMetadataUseCase.Request(request.filter)
            )
        ) { tasksUseCaseResult, metadataUseCaseResult ->
            Response(
                metadataResult = metadataUseCaseResult.metadataResult,
                relatedTasksGrouped = tasksUseCaseResult.relatedTasksGrouped
            )
        }

    data class Request(
        val filter: Filter.DateFilter,
        val taskCompleted: Boolean
    ): UseCase.Request

    data class Response(
        val metadataResult: MetadataResult,
        val relatedTasksGrouped: Map<String, RelatedTasksMetaDataResult>
    ): UseCase.Response
}