package com.zn.apps.domain.task

import android.database.sqlite.SQLiteException
import com.zn.apps.domain.UseCase
import com.zn.apps.domain.repository.TaskRepository
import com.zn.apps.model.data.task.Task
import com.zn.apps.model.usecase.UseCaseException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class DeleteTaskUseCase(
    configuration: Configuration,
    private val taskRepository: TaskRepository
): UseCase<DeleteTaskUseCase.Request, DeleteTaskUseCase.Response>(configuration) {

    override suspend fun process(request: Request): Flow<Response> {
        val response = try {
            taskRepository.deleteTask(request.task)
            Response
        } catch (exception: SQLiteException) {
            throw UseCaseException.TaskNotFoundException(exception)
        }
        return flowOf(response)
    }

    data class Request(val task: Task): UseCase.Request

    object Response: UseCase.Response
}