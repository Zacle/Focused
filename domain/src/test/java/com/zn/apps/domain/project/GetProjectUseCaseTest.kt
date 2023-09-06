package com.zn.apps.domain.project

import com.zn.apps.domain.repository.ProjectRepository
import com.zn.apps.domain.util.TestUtils.project_1
import com.zn.apps.model.usecase.UseCaseException
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

class GetProjectUseCaseTest {

    private val projectRepository = mock<ProjectRepository>()
    private val useCase = GetProjectUseCase(
        mock(),
        projectRepository
    )

    @Test
    fun `should retrieve a project matching the given id`() = runTest {
        val project = project_1
        val request = GetProjectUseCase.Request(project.id)
        whenever(projectRepository.getProject(project.id)).thenReturn(flowOf(project))
        val response = useCase.process(request).first()
        assertEquals(project, response.project)
    }

    @Test
    fun `should throw a project not found if no project matches the given id`() = runTest {
        val request = GetProjectUseCase.Request("no id")
        whenever(projectRepository.getProject("no id")).thenReturn(flowOf(null))
        try {
            useCase.process(request)
        } catch (e: Throwable) {
            assertTrue(e is UseCaseException.ProjectNotFoundException)
        }
    }
}