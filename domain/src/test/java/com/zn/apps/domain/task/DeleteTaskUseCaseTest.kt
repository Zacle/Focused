package com.zn.apps.domain.task

import android.database.sqlite.SQLiteException
import com.zn.apps.domain.repository.TaskRepository
import com.zn.apps.domain.util.TestUtils
import com.zn.apps.model.usecase.UseCaseException
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Test
import org.mockito.kotlin.doThrow
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify

class DeleteTaskUseCaseTest {

    private val taskRepository = mock<TaskRepository>()
    private val useCase = DeleteTaskUseCase(
        mock(),
        taskRepository
    )

    @Test
    fun `should be able to delete a task`() = runTest {
        val task = TestUtils.task_1_2
        val request = DeleteTaskUseCase.Request(task)
        useCase.process(request)
        verify(taskRepository).deleteTask(task)
    }

    @Test
    fun `should throw an error when deleting a non-existent task`() = runTest {
        val task = TestUtils.task_1_2
        val request = DeleteTaskUseCase.Request(task)
        doThrow(SQLiteException()).`when`(taskRepository).deleteTask(task)
        try {
            useCase.process(request)
        } catch (e: Throwable) {
            Assert.assertTrue(e is UseCaseException.TaskNotFoundException)
        }
    }
}