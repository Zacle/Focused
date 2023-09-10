package com.zn.apps.data_local.datasource

import com.zn.apps.data_local.database.project.PopulatedProjectEntity
import com.zn.apps.data_local.database.project.ProjectDao
import com.zn.apps.data_local.database.tag.TagEntity
import com.zn.apps.data_local.model.asEntity
import com.zn.apps.model.data.project.Project
import com.zn.apps.model.data.project.ProjectResource
import com.zn.apps.model.data.task.Task
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

class LocalProjectDataSourceTest {

    private val projectDao = mock<ProjectDao>()
    private val localProjectDataSource = LocalProjectDataSource(projectDao)

    @Test
    fun `should return an empty list of project resource`() = runTest {
        whenever(projectDao.getPopulatedProjects()).thenReturn(flowOf(emptyList()))
        val response = localProjectDataSource.getProjectResources().first()
        assertEquals(emptyList<ProjectResource>(), response)
    }

    @Test
    fun `should return a list of project with an empty tag name`() = runTest {
        val list = listOf(populatedProjectEntity_3)
        whenever(projectDao.getPopulatedProjects()).thenReturn(flowOf(list))
        val result = localProjectDataSource.getProjectResources().first()
        assertTrue(result[0].tagName.isEmpty())
    }

    @Test
    fun `should return a list of project with no tasks`() = runTest {
        val list = listOf(populatedProjectEntity_2)
        whenever(projectDao.getPopulatedProjects()).thenReturn(flowOf(list))
        val result = localProjectDataSource.getProjectResources().first()[0]
        assertEquals(result.numberOfTasks, 0)
        assertEquals(result.numberOfTasksCompleted, 0)
    }

    @Test
    fun `should return a list of project with a tag name`() = runTest {
        val list = listOf(populatedProjectEntity_1)
        whenever(projectDao.getPopulatedProjects()).thenReturn(flowOf(list))
        val result = localProjectDataSource.getProjectResources().first()[0]
        assertEquals("tag", result.tagName)
    }

    @Test
    fun `should return a list of project with tasks data`() = runTest {
        val list = listOf(populatedProjectEntity_1)
        whenever(projectDao.getPopulatedProjects()).thenReturn(flowOf(list))
        val result = localProjectDataSource.getProjectResources().first()[0]
        assertEquals(2, result.numberOfTasks)
        assertEquals(1, result.numberOfTasksCompleted)
    }

    @Test
    fun `should retrieve a project if id exists`() = runTest {
        val project = project_1
        whenever(projectDao.getProject(project.id)).thenReturn(flowOf(project.asEntity()))
        val result = localProjectDataSource.getProject(project.id).first()
        assertEquals(project, result)
    }

    @Test
    fun `should return null if id does not exists`() = runTest {
        whenever(projectDao.getProject("")).thenReturn(flowOf(null))
        val result = localProjectDataSource.getProject("").first()
        assertNull(result)
    }

    @Test
    fun `should upsert a project`() = runTest {
        localProjectDataSource.upsertProject(project_2)
        verify(projectDao).upsertProject(project_2.asEntity())
    }

    @Test
    fun `should delete a project`() = runTest {
        localProjectDataSource.deleteProject(project_2)
        verify(projectDao).deleteProject(project_2.asEntity())
    }
}


/************************************************************/
/**
 * Dummy data for tests
 */
val task_p_1 = Task(
    name = "task_p_1",
    completed = true
)
val task_p_2 = Task(
    name = "task_p_2"
)
val task_p_3 = Task(
    name = "task_p_3"
)
val tag = TagEntity(id = "tag_entity", name = "tag")

val project_1 = Project(name = "project_1", color = 1)
val project_2 = Project(name = "project_2", color = 2)
val project_3 = Project(name = "project_3", color = 3)

val populatedProjectEntity_1 = PopulatedProjectEntity(
    project = project_1.asEntity(),
    tags = listOf(tag),
    tasks = listOf(task_p_1, task_p_2).map { it.asEntity() }
)
val populatedProjectEntity_2 = PopulatedProjectEntity(
    project = project_2.asEntity(),
    tags = emptyList(),
    tasks = emptyList()
)
val populatedProjectEntity_3 = PopulatedProjectEntity(
    project = project_3.asEntity(),
    tags = emptyList(),
    tasks = listOf(task_p_3).map { it.asEntity() }
)

