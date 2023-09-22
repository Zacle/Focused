package com.zn.apps.domain.project

import com.zn.apps.common.MetadataResult
import com.zn.apps.domain.UseCase
import com.zn.apps.filter.Filter
import com.zn.apps.model.data.task.RelatedTasksMetaDataResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

class GetProjectTasksWithMetadata(
    configuration: Configuration,
    private val getProjectMetadataUseCase: GetProjectMetadataUseCase,
    private val getPopulatedProjectResourceUseCase: GetPopulatedProjectResourceUseCase
): UseCase<GetProjectTasksWithMetadata.Request, GetProjectTasksWithMetadata.Response>(configuration) {

    override suspend fun process(request: Request): Flow<Response> =
        combine(
            getProjectMetadataUseCase.process(
                GetProjectMetadataUseCase.Request(request.projectId)
            ),
            getPopulatedProjectResourceUseCase.process(
                GetPopulatedProjectResourceUseCase.Request(
                    projectId = request.projectId,
                    isTaskCompleted = request.isTaskCompleted,
                    filter = request.filter
                )
            )
        ) { metadataUseCaseResult, populatedProjectUseCaseResult ->
            Response(
                metadataResult = metadataUseCaseResult.metadataResult,
                relatedTasksGrouped = populatedProjectUseCaseResult.relatedTasksGrouped
            )
        }

    data class Request(
        val filter: Filter,
        val projectId: String,
        val isTaskCompleted: Boolean
    ): UseCase.Request

    data class Response(
        val metadataResult: MetadataResult,
        val relatedTasksGrouped: Map<String, RelatedTasksMetaDataResult>
    ): UseCase.Response
}