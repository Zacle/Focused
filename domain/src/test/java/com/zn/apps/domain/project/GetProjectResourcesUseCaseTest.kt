package com.zn.apps.domain.project

import com.zn.apps.domain.repository.ProjectRepository
import com.zn.apps.domain.util.TestUtils.projectResource_1
import com.zn.apps.domain.util.TestUtils.projectResource_2
import com.zn.apps.domain.util.TestUtils.projectResource_3
import com.zn.apps.domain.util.TestUtils.projectResource_4
import com.zn.apps.model.data.project.ProjectFilterType
import com.zn.apps.model.data.project.ProjectFiltration
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

class GetProjectResourcesUseCaseTest {

    private val projectRepository = mock<ProjectRepository>()
    private val useCase = GetProjectResourcesUseCase(
        mock(),
        projectRepository
    )

    @Test
    fun `should return all projects`() = runTest {
        val projects = listOf(projectResource_1, projectResource_2)
        val filter = ProjectFiltration(
            filterType = ProjectFilterType.ALL,
            query = ""
        )
        val request = GetProjectResourcesUseCase.Request(filter)
        whenever(projectRepository.getProjectResources()).thenReturn(flowOf(projects))
        val response = useCase.process(request).first()
        assertEquals(projects, response.projectResources)
    }

    @Test
    fun `should return all completed projects that matches the empty query`() = runTest {
        val projects = listOf(projectResource_1, projectResource_2, projectResource_3, projectResource_4)
        val filter = ProjectFiltration(
            filterType = ProjectFilterType.COMPLETED,
            query = ""
        )
        val request = GetProjectResourcesUseCase.Request(filter)
        whenever(projectRepository.getProjectResources()).thenReturn(flowOf(projects))
        val actual = useCase.process(request).first()
        val expected = GetProjectResourcesUseCase.Response(listOf(projectResource_3))
        assertEquals(expected, actual)
    }

    @Test
    fun `should return all completed projects that matches the query`() = runTest {
        val projects = listOf(projectResource_1, projectResource_2, projectResource_3, projectResource_4)
        val filter = ProjectFiltration(
            filterType = ProjectFilterType.COMPLETED,
            query = "ma"
        )
        val request = GetProjectResourcesUseCase.Request(filter)
        whenever(projectRepository.getProjectResources()).thenReturn(flowOf(projects))
        val actual = useCase.process(request).first()
        val expected = GetProjectResourcesUseCase.Response(listOf(projectResource_3))
        assertEquals(expected, actual)
    }

    @Test
    fun `should return empty list if all completed projects don't match the query`() = runTest {
        val projects = listOf(projectResource_1, projectResource_2, projectResource_3, projectResource_4)
        val filter = ProjectFiltration(
            filterType = ProjectFilterType.COMPLETED,
            query = "Skill"
        )
        val request = GetProjectResourcesUseCase.Request(filter)
        whenever(projectRepository.getProjectResources()).thenReturn(flowOf(projects))
        val actual = useCase.process(request).first()
        val expected = GetProjectResourcesUseCase.Response(emptyList())
        assertEquals(expected, actual)
    }

    @Test
    fun `should return all ongoing projects that matches the empty query`() = runTest {
        val projects = listOf(projectResource_1, projectResource_2, projectResource_3, projectResource_4)
        val filter = ProjectFiltration(
            filterType = ProjectFilterType.ONGOING,
            query = ""
        )
        val request = GetProjectResourcesUseCase.Request(filter)
        whenever(projectRepository.getProjectResources()).thenReturn(flowOf(projects))
        val actual = useCase.process(request).first()
        val expected = GetProjectResourcesUseCase.Response(
            listOf(projectResource_1, projectResource_2, projectResource_4)
        )
        assertEquals(expected, actual)
    }

    @Test
    fun `should return all ongoing projects that matches the query`() = runTest {
        val projects = listOf(projectResource_1, projectResource_2, projectResource_3, projectResource_4)
        val filter = ProjectFiltration(
            filterType = ProjectFilterType.ONGOING,
            query = "ma"
        )
        val request = GetProjectResourcesUseCase.Request(filter)
        whenever(projectRepository.getProjectResources()).thenReturn(flowOf(projects))
        val actual = useCase.process(request).first()
        val expected = GetProjectResourcesUseCase.Response(
            listOf(projectResource_2)
        )
        assertEquals(expected, actual)
    }

    @Test
    fun `should return empty list if all ongoing projects don't match the query`() = runTest {
        val projects = listOf(projectResource_1, projectResource_2, projectResource_3, projectResource_4)
        val filter = ProjectFiltration(
            filterType = ProjectFilterType.ONGOING,
            query = "os"
        )
        val request = GetProjectResourcesUseCase.Request(filter)
        whenever(projectRepository.getProjectResources()).thenReturn(flowOf(projects))
        val actual = useCase.process(request).first()
        val expected = GetProjectResourcesUseCase.Response(
            emptyList()
        )
        assertEquals(expected, actual)
    }

    @Test
    fun `should return all projects that matches the query`() = runTest {
        val projects = listOf(projectResource_1, projectResource_2, projectResource_3, projectResource_4)
        val filter = ProjectFiltration(
            filterType = ProjectFilterType.ALL,
            query = "ma"
        )
        val request = GetProjectResourcesUseCase.Request(filter)
        whenever(projectRepository.getProjectResources()).thenReturn(flowOf(projects))
        val actual = useCase.process(request).first()
        val expected = GetProjectResourcesUseCase.Response(
            listOf(projectResource_2, projectResource_3)
        )
        assertEquals(expected, actual)
    }

    @Test
    fun `should return empty list if all projects don't match the query`() = runTest {
        val projects = listOf(projectResource_1, projectResource_2, projectResource_3, projectResource_4)
        val filter = ProjectFiltration(
            filterType = ProjectFilterType.ALL,
            query = "fintech"
        )
        val request = GetProjectResourcesUseCase.Request(filter)
        whenever(projectRepository.getProjectResources()).thenReturn(flowOf(projects))
        val actual = useCase.process(request).first()
        val expected = GetProjectResourcesUseCase.Response(
            emptyList()
        )
        assertEquals(expected, actual)
    }
}