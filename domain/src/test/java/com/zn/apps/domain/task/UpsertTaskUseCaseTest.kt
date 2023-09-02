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

class UpsertTaskUseCaseTest {

    private val taskRepository = mock<TaskRepository>()
    private val useCase = UpsertTaskUseCase(
        mock(),
        taskRepository
    )


    @Test
    fun `should be able to add a new task`() = runTest {
        val task = TestUtils.task_1_1
        val request = UpsertTaskUseCase.Request(task)
        useCase.process(request)
        verify(taskRepository).upsertTask(task)
    }

    @Test
    fun `adding or modifying a non existent task should throw a task not found exception`() = runTest {
        val task = TestUtils.task_1_1
        val request = UpsertTaskUseCase.Request(task)
        doThrow(SQLiteException()).`when`(taskRepository).upsertTask(task)
        try {
            useCase.process(request)
        } catch (ex: Throwable) {
            Assert.assertTrue(ex is UseCaseException.TaskNotUpdatedException)
        }
    }
}