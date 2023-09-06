package com.zn.apps.data_local.datasource

import com.zn.apps.data_local.database.project.ProjectEntity
import com.zn.apps.data_local.database.tag.TagEntity
import com.zn.apps.data_local.database.task.PopulatedTaskEntity
import com.zn.apps.data_local.database.task.TaskDao
import com.zn.apps.data_local.model.asEntity
import com.zn.apps.model.data.task.Task
import com.zn.apps.model.data.task.TaskResource
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import java.time.OffsetDateTime

class LocalTaskDataSourceTest {

    private val taskDao = mock<TaskDao>()
    private val localTaskDataSource = LocalTaskDataSource(taskDao)

    @Test
    fun `Given an empty list of tasks, return an empty list of task resources`() = runTest {
        whenever(taskDao.getTasks(false)).thenReturn(flowOf(emptyList()))
        val response = localTaskDataSource.getTasks(false).first()
        assertEquals(emptyList<TaskResource>(), response)
    }

    @Test
    fun `Given a task with empty project, the projectId should be null`() = runTest {
        whenever(taskDao.getTask("")).thenReturn(flowOf(populatedTaskEntity_1))
        val result = localTaskDataSource.getTask("").first()
        assertNull(result?.projectId)
    }

    @Test
    fun `Given a task with empty project, the project name should be empty`() = runTest {
        whenever(taskDao.getTask("")).thenReturn(flowOf(populatedTaskEntity_1))
        val result = localTaskDataSource.getTask("").first()
        assertEquals("", result?.projectName)
    }

    @Test
    fun `Given a task with empty tag, the tagId should be null`() = runTest {
        whenever(taskDao.getTask("")).thenReturn(flowOf(populatedTaskEntity_3))
        val result = localTaskDataSource.getTask("").first()
        assertNull(result?.tagId)
    }

    @Test
    fun `Given a task with empty tag, the tagName should be empty`() = runTest {
        whenever(taskDao.getTask("")).thenReturn(flowOf(populatedTaskEntity_3))
        val result = localTaskDataSource.getTask("").first()
        assertEquals("", result?.tagName)
    }

    @Test
    fun `Given a task with a non empty project, the project data should exist`() = runTest {
        whenever(taskDao.getTask("")).thenReturn(flowOf(populatedTaskEntity_3))
        val result = localTaskDataSource.getTask("").first()
        assertEquals(taskResource_3, result)
    }

    @Test
    fun `Given a task with a non empty tag, the tag data should exist`() = runTest {
        whenever(taskDao.getTask("")).thenReturn(flowOf(populatedTaskEntity_2))
        val result = localTaskDataSource.getTask("").first()
        assertEquals(taskResource_2, result)
    }

    @Test
    fun `Given a list of embedded tasks, return the task resources`()  = runTest {
        whenever(taskDao.getTasks(false))
            .thenReturn(
                flowOf(listOf(populatedTaskEntity_2, populatedTaskEntity_3, populatedTaskEntity_4))
            )
        val result = localTaskDataSource.getTasks(false).first()
        assertEquals(
            listOf(taskResource_2, taskResource_3, taskResource_4),
            result
        )
    }

    @Test
    fun `should be able to return a null task resource for non existent taskId`() = runTest {
        whenever(taskDao.getTask("")).thenReturn(flowOf(null))
        val result = localTaskDataSource.getTask("").first()
        assertNull(result)
    }

    @Test
    fun `Should be able to insert a task`() = runTest {
        whenever(taskDao.upsertTask(taskEntity)).thenReturn(Unit)
        localTaskDataSource.upsertTask(task)
        verify(taskDao).upsertTask(taskEntity)
    }

    @Test
    fun `Should be able to delete a task`() = runTest {
        whenever(taskDao.deleteTask(taskEntity)).thenReturn(Unit)
        localTaskDataSource.deleteTask(task)
        verify(taskDao).deleteTask(taskEntity)
    }
}

/************************************************************/
/**
 * Dummy data for tests
 */
val task_1 = Task(
    name = "populatedTaskEntity_1"
)
val populatedTaskEntity_1 = PopulatedTaskEntity(
    task = task_1.asEntity(),
    tag = emptyList(),
    project = emptyList()
)
val taskResource_1 = TaskResource(
    task = task_1
)
val task_2 = Task(
    name = "populatedTaskEntity_2",
    tagId = "default"
)
val populatedTaskEntity_2 = PopulatedTaskEntity(
    task = task_2.asEntity(),
    tag = listOf(TagEntity(id = "default", name = "tag")),
    project = emptyList()
)
val taskResource_2 = TaskResource(
    task = task_2,
    tagId = "default",
    tagName = "tag"
)
val task_3 = Task(
    name = "populatedTaskEntity_3",
    projectId = "default"
)
val populatedTaskEntity_3 = PopulatedTaskEntity(
    task = task_3.asEntity(),
    tag = emptyList(),
    project = listOf(ProjectEntity(
        name = "project",
        id = "default",
        color = 0,
        createdAt = OffsetDateTime.now()
    )
    )
)
val taskResource_3 = TaskResource(
    task = task_3,
    projectId = "default",
    projectName = "project"
)
val task_4 = Task(
    name = "populatedTaskEntity_4",
    tagId = "defaultTag",
    projectId = "defaultProject"
)
val populatedTaskEntity_4 = PopulatedTaskEntity(
    task = task_4.asEntity(),
    tag = listOf(TagEntity(id = "defaultTag", name = "tag")),
    project = listOf(ProjectEntity(
        name = "project",
        id = "default",
        color = 0,
        createdAt = OffsetDateTime.now()
    ))
)
val taskResource_4 = TaskResource(
    task = task_4,
    tagId = "defaultTag",
    tagName = "tag",
    projectId = "default",
    projectName = "project"
)
val task = Task(name = "test")
val taskEntity = task.asEntity()
/**************************************************/