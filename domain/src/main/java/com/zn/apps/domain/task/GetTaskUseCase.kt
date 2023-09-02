package com.zn.apps.domain.task

import com.zn.apps.domain.UseCase
import com.zn.apps.domain.repository.TaskRepository
import com.zn.apps.model.data.task.TaskResource
import com.zn.apps.model.usecase.UseCaseException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetTaskUseCase(
    configuration: Configuration,
    private val taskRepository: TaskRepository
): UseCase<GetTaskUseCase.Request, GetTaskUseCase.Response>(configuration) {

    override suspend fun process(request: Request): Flow<Response> {
        return taskRepository.getTaskResource(request.taskId)
            .map {
                if (it != null) {
                    Response(it)
                } else {
                    throw UseCaseException.TaskNotFoundException(Throwable())
                }
            }
    }

    data class Request(val taskId: String): UseCase.Request

    data class Response(val taskResource: TaskResource): UseCase.Response
}