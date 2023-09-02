package com.zn.apps.domain.tag

import com.zn.apps.domain.UseCase
import com.zn.apps.domain.repository.TagRepository
import com.zn.apps.model.data.tag.Tag
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetTagsUseCase(
    configuration: Configuration,
    private val tagRepository: TagRepository
): UseCase<GetTagsUseCase.Request, GetTagsUseCase.Response>(configuration) {

    override suspend fun process(request: Request): Flow<Response> {
        return tagRepository.getTags().map { Response(it) }
    }

    object Request: UseCase.Request

    data class Response(val tags: List<Tag>): UseCase.Response
}