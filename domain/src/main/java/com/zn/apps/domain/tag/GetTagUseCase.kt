package com.zn.apps.domain.tag

import com.zn.apps.domain.UseCase
import com.zn.apps.domain.repository.TagRepository
import com.zn.apps.model.data.tag.Tag
import com.zn.apps.model.usecase.UseCaseException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetTagUseCase(
    configuration: Configuration,
    private val tagRepository: TagRepository
): UseCase<GetTagUseCase.Request, GetTagUseCase.Response>(configuration) {

    override suspend fun process(request: Request): Flow<Response> {
        return tagRepository.getTag(request.tagId)
            .map {
                if (it != null) {
                    Response(it)
                } else {
                    throw UseCaseException.TagNotFoundException(Throwable())
                }
            }
    }

    data class Request(val tagId: String): UseCase.Request

    data class Response(val tag: Tag): UseCase.Response
}