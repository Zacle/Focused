package com.zn.apps.domain.project

import android.database.sqlite.SQLiteException
import com.zn.apps.domain.repository.ProjectRepository
import com.zn.apps.domain.util.TestUtils
import com.zn.apps.model.usecase.UseCaseException
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertTrue
import org.junit.Test
import org.mockito.kotlin.doThrow
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify

class UpsertProjectUseCaseTest {

    private val projectRepository = mock<ProjectRepository>()
    private val useCase = UpsertProjectUseCase(
        mock(),
        projectRepository
    )

    @Test
    fun `should add or modify a project`() = runTest {
        val project = TestUtils.project_2
        val request = UpsertProjectUseCase.Request(project)
        useCase.process(request)
        verify(projectRepository).upsertProject(project)
    }

    @Test
    fun `adding or modifying a non existent project should throw a project not found exception`() = runTest {
        val project = TestUtils.project_2
        val request = UpsertProjectUseCase.Request(project)
        doThrow(SQLiteException()).`when`(projectRepository).upsertProject(project)
        try {
            useCase.process(request)
        } catch (e: Throwable) {
            assertTrue(e is UseCaseException.ProjectNotUpdatedException)
        }
    }
}