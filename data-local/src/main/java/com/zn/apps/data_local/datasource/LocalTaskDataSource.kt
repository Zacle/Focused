package com.zn.apps.data_local.datasource

import com.zn.apps.data_local.database.task.PopulatedTaskEntity
import com.zn.apps.data_local.database.task.TaskDao
import com.zn.apps.data_local.model.asEntity
import com.zn.apps.data_local.model.asExternalModel
import com.zn.apps.data_repository.data_source.local.TaskDataSource
import com.zn.apps.model.data.task.Task
import com.zn.apps.model.data.task.TaskResource
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.mapLatest
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
class LocalTaskDataSource @Inject constructor(
    private val taskDao: TaskDao
): TaskDataSource {
    override fun getTasks(completed: Boolean): Flow<List<TaskResource>> =
        taskDao.getTasks(completed).mapLatest { tasks ->
            tasks.mapTo(mutableListOf()) { populatedTaskEntity ->
                val (projectId, projectName, projectCompleted) = getProjectIdAndName(populatedTaskEntity)
                val (tagId, tagName) = getTagIdAndName(populatedTaskEntity)
                TaskResource(
                    task = populatedTaskEntity.task.asExternalModel(),
                    projectId = projectId,
                    projectName = projectName,
                    projectCompleted = projectCompleted,
                    tagId = tagId,
                    tagName = tagName
                )
            }
        }

    private fun getProjectIdAndName(
        populatedTaskEntity: PopulatedTaskEntity
    ): Triple<String?, String, Boolean> {
        return if (populatedTaskEntity.project.isEmpty()) {
            Triple(null, "", false)
        }
        else {
            Triple(
                populatedTaskEntity.project[0].id,
                populatedTaskEntity.project[0].name,
                populatedTaskEntity.project[0].completed
            )
        }
    }

    private fun getTagIdAndName(populatedTaskEntity: PopulatedTaskEntity): Pair<String?, String> {
        return if (populatedTaskEntity.tag.isEmpty()) {
            Pair(null, "")
        }
        else {
            Pair(populatedTaskEntity.tag[0].id, populatedTaskEntity.tag[0].name)
        }
    }

    override fun getTask(taskId: String): Flow<TaskResource?> =
        taskDao.getTask(taskId).mapLatest { populatedTaskEntity ->
            if (populatedTaskEntity == null) {
                null
            }
            else {
                val (projectId, projectName, projectCompleted) = getProjectIdAndName(populatedTaskEntity)
                val (tagId, tagName) = getTagIdAndName(populatedTaskEntity)
                TaskResource(
                    task = populatedTaskEntity.task.asExternalModel(),
                    projectId = projectId,
                    projectName = projectName,
                    projectCompleted = projectCompleted,
                    tagId = tagId,
                    tagName = tagName
                )
            }
        }

    override suspend fun upsertTask(task: Task) {
        taskDao.upsertTask(task.asEntity())
    }

    override suspend fun deleteTask(task: Task) {
        taskDao.deleteTask(task.asEntity())
    }
}