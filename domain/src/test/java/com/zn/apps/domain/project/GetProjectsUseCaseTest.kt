package com.zn.apps.domain.project

import com.zn.apps.domain.repository.ProjectRepository
import com.zn.apps.domain.util.TestUtils.projectResource_1
import com.zn.apps.domain.util.TestUtils.projectResource_2
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

class GetProjectsUseCaseTest {

    private val projectRepository = mock<ProjectRepository>()
    private val useCase = GetProjectsUseCase(
        mock(),
        projectRepository
    )

    @Test
    fun `should return all projects`() = runTest {
        val projects = listOf(projectResource_1, projectResource_2)
        val request = GetProjectsUseCase.Request
        whenever(projectRepository.getProjectResources()).thenReturn(flowOf(projects))
        val response = useCase.process(request).first()
        assertEquals(projects, response.projectResources)
    }
}