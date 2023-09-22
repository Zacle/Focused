package com.zn.apps.domain.repository

import com.zn.apps.model.data.task.Task
import com.zn.apps.model.data.task.TaskResource
import kotlinx.coroutines.flow.Flow

interface TaskRepository {

    /**
     * Get all task resources with no filter
     */
    fun getTasks(): Flow<List<TaskResource>>

    /**
     * Get task resources. Task Resources are embedded with project and tag to display
     * on the screen
     *
     * @param completed filter task based on their completion
     */
    fun getTaskResources(completed: Boolean): Flow<List<TaskResource>>

    /**
     * Get the task resource that matches the task id
     *
     * @param taskId
     */
    fun getTaskResource(taskId: String): Flow<TaskResource?>

    /**
     * Update the task if it already exists or insert to the database otherwise
     *
     * @param task
     */
    suspend fun upsertTask(task: Task)

    /**
     * Delete the task from the database
     *
     * @param task
     */
    suspend fun deleteTask(task: Task)

}