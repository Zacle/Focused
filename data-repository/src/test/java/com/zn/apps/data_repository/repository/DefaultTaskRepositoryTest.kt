package com.zn.apps.data_repository.repository

import com.zn.apps.data_repository.data_source.local.TaskDataSource
import com.zn.apps.data_repository.util.TestUtils.taskResource_1_1
import com.zn.apps.data_repository.util.TestUtils.taskResource_1_2
import com.zn.apps.data_repository.util.TestUtils.taskResource_1_3
import com.zn.apps.data_repository.util.TestUtils.taskResource_1_4
import com.zn.apps.data_repository.util.TestUtils.task_1_1
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

class DefaultTaskRepositoryTest {

    private val dataSource = mock<TaskDataSource>()
    private val defaultTaskRepository = DefaultTaskRepository(dataSource)

    @Test
    fun testGetTaskResources() = runTest {
        val tasks = listOf(taskResource_1_1, taskResource_1_2, taskResource_1_3)
        whenever(dataSource.getTasks(false)).thenReturn(flowOf(tasks))
        val result = defaultTaskRepository.getTaskResources(false).first()
        assertEquals(tasks, result)
        verify(dataSource).getTasks(false)
    }

    @Test
    fun testGetTaskResource() = runTest {
        val taskResource = taskResource_1_4
        whenever(dataSource.getTask(taskResource.task.id)).thenReturn(flowOf(taskResource))
        val result = defaultTaskRepository.getTaskResource(taskResource.task.id).first()
        assertEquals(taskResource, result)
        verify(dataSource).getTask(taskResource.task.id)
    }

    @Test
    fun testUpsertTask() = runTest {
        val task = task_1_1
        defaultTaskRepository.upsertTask(task)
        verify(dataSource).upsertTask(task)
    }

    @Test
    fun testDeleteTask() = runTest {
        val task = task_1_1
        defaultTaskRepository.deleteTask(task)
        verify(dataSource).deleteTask(task)
    }
}