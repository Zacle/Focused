package com.zn.apps.domain.tag

import com.zn.apps.domain.UseCase
import com.zn.apps.domain.repository.TagRepository
import com.zn.apps.model.data.tag.Tag
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class SearchTagUseCase(
    configuration: Configuration,
    private val tagRepository: TagRepository
): UseCase<SearchTagUseCase.Request, SearchTagUseCase.Response>(configuration) {

    override suspend fun process(request: Request): Flow<Response> {
        return tagRepository.searchTag(request.query).map { Response(it) }
    }

    data class Request(val query: String): UseCase.Request

    data class Response(val tags: List<Tag>): UseCase.Response
}