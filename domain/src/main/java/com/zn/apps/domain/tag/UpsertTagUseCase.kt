package com.zn.apps.domain.tag

import android.database.sqlite.SQLiteException
import com.zn.apps.domain.UseCase
import com.zn.apps.domain.repository.TagRepository
import com.zn.apps.model.data.tag.Tag
import com.zn.apps.model.usecase.UseCaseException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class UpsertTagUseCase(
    configuration: Configuration,
    private val tagRepository: TagRepository
): UseCase<UpsertTagUseCase.Request, UpsertTagUseCase.Response>(configuration) {

    override suspend fun process(request: Request): Flow<Response> {
        val response = try {
            tagRepository.upsertTag(request.tag)
            Response
        } catch (exception: SQLiteException) {
            throw UseCaseException.TagNotUpdatedException(exception)
        }
        return flowOf(response)
    }

    data class Request(val tag: Tag): UseCase.Request

    object Response: UseCase.Response
}