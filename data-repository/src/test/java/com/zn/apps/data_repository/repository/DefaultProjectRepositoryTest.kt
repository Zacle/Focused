package com.zn.apps.data_repository.repository

import com.zn.apps.data_repository.data_source.local.ProjectDataSource
import com.zn.apps.data_repository.util.TestUtils.projectResource_1
import com.zn.apps.data_repository.util.TestUtils.projectResource_2
import com.zn.apps.data_repository.util.TestUtils.project_1
import com.zn.apps.data_repository.util.TestUtils.project_2
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

class DefaultProjectRepositoryTest {

    private val projectDataSource = mock<ProjectDataSource>()
    private val repository = DefaultProjectRepository(projectDataSource)

    @Test
    fun testGetProjectResources()  = runTest {
        val projects = listOf(projectResource_1, projectResource_2)
        whenever(projectDataSource.getProjectResources()).thenReturn(flowOf(projects))
        val response = repository.getProjectResources().first()
        assertEquals(projects, response)
    }

    @Test
    fun testGetProject() = runTest {
        val project = project_1
        whenever(projectDataSource.getProject(project.id)).thenReturn(flowOf(project))
        val response = repository.getProject(project.id).first()
        assertEquals(project, response)
    }

    @Test
    fun testUpsertTask() = runTest {
        val project = project_2
        repository.upsertProject(project)
        verify(projectDataSource).upsertProject(project)
    }

    @Test
    fun testDeleteProject() = runTest {
        val project = project_2
        repository.deleteProject(project)
        verify(projectDataSource).deleteProject(project)
    }
}