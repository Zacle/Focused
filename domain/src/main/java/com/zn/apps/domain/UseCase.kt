package com.zn.apps.domain

import com.zn.apps.model.usecase.Result
import com.zn.apps.model.usecase.UseCaseException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map

abstract class UseCase<I: UseCase.Request, O: UseCase.Response>(private val configuration: Configuration) {

    suspend fun execute(request: I) = process(request)
        .map<O, Result<O>> {
            Result.Success(it)
        }
        .flowOn(configuration.dispatcher)
        .catch {
            emit(Result.Error(UseCaseException.createFromThrowable(it)))
        }

    internal abstract suspend fun process(request: I): Flow<O>

    class Configuration(val dispatcher: CoroutineDispatcher)

    interface Request

    interface Response
}