package com.zn.apps.data_repository.data_source.local

import com.zn.apps.model.data.task.Task
import com.zn.apps.model.data.task.TaskResource
import kotlinx.coroutines.flow.Flow

/**
 * Main entry point for accessing the tasks in the database: either
 * local or remote
 */
interface TaskDataSource {

    /**
     * Get all task resources with no filter
     */
    fun getTasks(): Flow<List<TaskResource>>

    /**
     * Get all tasks
     */
    fun getTasks(completed: Boolean): Flow<List<TaskResource>>

    /**
     * Get a task that match the given id
     *
     * @param taskId
     */
    fun getTask(taskId: String): Flow<TaskResource?>

    /**
     * Add or modify the task
     *
     * @param task
     */
    suspend fun upsertTask(task: Task)

    /**
     * Delete the task
     *
     * @param task
     */
    suspend fun deleteTask(task: Task)
}