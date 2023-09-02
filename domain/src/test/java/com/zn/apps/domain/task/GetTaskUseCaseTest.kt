package com.zn.apps.domain.task

import com.zn.apps.domain.repository.TaskRepository
import com.zn.apps.domain.util.TestUtils
import com.zn.apps.model.usecase.UseCaseException
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

class GetTaskUseCaseTest {

    private val taskRepository = mock<TaskRepository>()
    private val useCase = GetTaskUseCase(
        mock(),
        taskRepository
    )

    @Test
    fun `If the project id is null, the name must be empty and completed must be false`() = runTest {
        val task = TestUtils.task_1_1
        val request = GetTaskUseCase.Request(task.id)
        whenever(taskRepository.getTaskResource(request.taskId)).thenReturn(
            flowOf(TestUtils.taskResource_1_1)
        )
        val response = useCase.process(request).first()
        assertNull(response.taskResource.projectId)
        assertEquals(response.taskResource.projectName, "")
    }

    @Test
    fun `If the tag id is null, the name must be empty`() = runTest {
        val task = TestUtils.task_1_4
        val request = GetTaskUseCase.Request(task.id)
        whenever(taskRepository.getTaskResource(request.taskId)).thenReturn(
            flowOf(TestUtils.taskResource_1_4)
        )
        val response = useCase.process(request).first()
        assertNull(response.taskResource.tagId)
        assertEquals(response.taskResource.tagName, "")
    }

    @Test
    fun `If the task id is not found, should throw task not found exception`() = runTest {
        val task = TestUtils.task_1_2
        val request = GetTaskUseCase.Request(task.id)
        whenever(taskRepository.getTaskResource(request.taskId)).thenReturn(
            flowOf(null)
        )
        try {
            useCase.process(request)
        } catch (e: Throwable) {
            Assert.assertTrue(e is UseCaseException.TaskNotFoundException)
        }
    }
}