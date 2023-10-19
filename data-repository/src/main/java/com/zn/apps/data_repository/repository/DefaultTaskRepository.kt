package com.zn.apps.data_repository.repository

import com.zn.apps.data_repository.data_source.local.TaskDataSource
import com.zn.apps.domain.repository.TaskRepository
import com.zn.apps.model.data.task.Task
import com.zn.apps.model.data.task.TaskResource
import kotlinx.coroutines.flow.Flow

class DefaultTaskRepository(
    private val datasource: TaskDataSource
): TaskRepository {

    override fun getTasks(): Flow<List<TaskResource>> = datasource.getTasks()

    override fun getTaskResources(completed: Boolean): Flow<List<TaskResource>> =
        datasource.getTasks(completed)

    override fun getTaskResources(from: String, to: String): Flow<List<TaskResource>> =
        datasource.getTasks(from, to)

    override fun getTaskResource(taskId: String): Flow<TaskResource?> = datasource.getTask(taskId)

    override suspend fun upsertTask(task: Task) {
        datasource.upsertTask(task)
    }

    override suspend fun deleteTask(task: Task) {
        datasource.deleteTask(task)
    }
}