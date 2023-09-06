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

class DeleteProjectUseCaseTest {

    private val projectRepository = mock<ProjectRepository>()
    private val useCase = DeleteProjectUseCase(
        mock(),
        projectRepository
    )

    @Test
    fun `should delete a project`() = runTest {
        val project = TestUtils.project_1
        val request = DeleteProjectUseCase.Request(project)
        useCase.process(request)
        verify(projectRepository).deleteProject(project)
    }

    @Test
    fun `should throw an error when deleting a non-existent project`() = runTest {
        val project = TestUtils.project_1
        val request = DeleteProjectUseCase.Request(project)
        doThrow(SQLiteException()).`when`(projectRepository).deleteProject(project)
        try {
            useCase.process(request)
        } catch (e: Throwable) {
            assertTrue(e is UseCaseException.ProjectNotFoundException)
        }
    }
}