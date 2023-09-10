package com.zn.apps.data_local.database.project

import androidx.test.filters.MediumTest
import com.google.common.truth.Truth.assertThat
import com.zn.apps.data_local.database.FocusedDatabase
import com.zn.apps.data_local.database.tag.TagDao
import com.zn.apps.data_local.database.task.TaskDao
import com.zn.apps.data_local.database.util.TaskEntities.designTag
import com.zn.apps.data_local.database.util.TaskEntities.project_1
import com.zn.apps.data_local.database.util.TaskEntities.project_2
import com.zn.apps.data_local.database.util.TaskEntities.project_3
import com.zn.apps.data_local.database.util.TaskEntities.project_4
import com.zn.apps.data_local.database.util.TaskEntities.sportTag
import com.zn.apps.data_local.database.util.TaskEntities.studyTag
import com.zn.apps.data_local.database.util.TaskEntities.taskEntity_1
import com.zn.apps.data_local.database.util.TaskEntities.taskEntity_3
import com.zn.apps.data_local.database.util.TaskEntities.taskEntity_4
import com.zn.apps.data_local.model.asEntity
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject
import javax.inject.Named

@MediumTest
@HiltAndroidTest
class ProjectDaoTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Inject
    @Named("test_db")
    lateinit var database: FocusedDatabase

    private lateinit var tagDao: TagDao
    private lateinit var taskDao: TaskDao
    private lateinit var projectDao: ProjectDao

    @Before
    fun setup() {
        hiltRule.inject()
        tagDao = database.tagDao()
        taskDao = database.taskDao()
        projectDao = database.projectDao()
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun shouldReturnEmpty() = runTest {
        val result = projectDao.getPopulatedProjects().first()
        assertThat(result).isEmpty()
    }

    @Test
    fun returnProjectWithNoTag() = runTest {
        projectDao.insertAllProjects(listOf(project_2, project_3).map { it.asEntity() })
        val actual = projectDao.getPopulatedProjects().first()
        val expected = listOf(
            PopulatedProjectEntity(
                project = project_2.asEntity(),
                tags = emptyList(),
                tasks = emptyList()
            ),
            PopulatedProjectEntity(
                project = project_3.asEntity(),
                tags = emptyList(),
                tasks = emptyList()
            )
        )
        assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun returnProjectWithTagEmbedded() = runTest {
        tagDao.insertAllTags(listOf(studyTag, sportTag))
        projectDao.insertAllProjects(listOf(project_1, project_2).map { it.asEntity() })
        val actual = projectDao.getPopulatedProjects().first()
        val expected = listOf(
            PopulatedProjectEntity(
                project = project_1.asEntity(),
                tags = listOf(studyTag),
                tasks = emptyList()
            ),
            PopulatedProjectEntity(
                project = project_2.asEntity(),
                tags = emptyList(),
                tasks = emptyList()
            )
        )
        assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun returnProjectWithNoTasks() = runTest {
        projectDao.insertAllProjects(listOf(project_2, project_3).map { it.asEntity() })
        val actual = projectDao.getPopulatedProjects().first()
        val expected = listOf(
            PopulatedProjectEntity(
                project = project_2.asEntity(),
                tags = emptyList(),
                tasks = emptyList()
            ),
            PopulatedProjectEntity(
                project = project_3.asEntity(),
                tags = emptyList(),
                tasks = emptyList()
            )
        )
        assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun returnProjectWithTasksEmbedded() = runTest {
        tagDao.insertAllTags(listOf(studyTag, designTag, sportTag))
        projectDao.insertAllProjects(listOf(project_1, project_2, project_4).map { it.asEntity() })
        val tasks = listOf(taskEntity_1.copy(projectId = project_1.id), taskEntity_4.copy(projectId = project_4.id))
        taskDao.insertTasks(tasks)
        val actual = projectDao.getPopulatedProjects().first()
        val expected = listOf(
            PopulatedProjectEntity(
                project = project_1.asEntity(),
                tags = listOf(studyTag),
                tasks = listOf(taskEntity_1)
            ),
            PopulatedProjectEntity(
                project = project_2.asEntity(),
                tags = emptyList(),
                tasks = emptyList()
            ),
            PopulatedProjectEntity(
                project = project_4.asEntity(),
                tags = listOf(designTag),
                tasks = listOf(taskEntity_4)
            )
        )
        assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun shouldInsertProject() = runTest {
        projectDao.upsertProject(project_2.asEntity())
        assertThat(projectDao.getProject(project_2.id).first()).isNotNull()
    }

    @Test
    fun shouldModifyExistingProject() = runTest {
        val name = "updated"
        projectDao.upsertProject(project_2.asEntity())
        val project = project_2.asEntity().copy(name = name)
        projectDao.upsertProject(project)
        assertThat(projectDao.getProject(project_2.id).first()?.name).isEqualTo(name)
    }

    @Test
    fun shouldDeleteProject() = runTest {
        tagDao.insertAllTags(listOf(studyTag, designTag, sportTag))
        projectDao.insertAllProjects(listOf(project_1, project_2, project_4).map { it.asEntity() })
        projectDao.deleteProject(project_1.asEntity())
        assertThat(projectDao.getProject(project_1.id).first()).isNull()
    }

    @Test
    fun deletingProjectShouldDeleteTasks() = runTest {
        tagDao.insertAllTags(listOf(studyTag, designTag, sportTag))
        projectDao.insertAllProjects(listOf(project_1, project_2, project_4).map { it.asEntity() })
        val tasks = listOf(taskEntity_1.copy(projectId = project_1.id), taskEntity_3.copy(projectId = project_1.id))
        taskDao.insertTasks(tasks)
        projectDao.deleteProject(project_1.asEntity())
        assertThat(taskDao.getTasks(false).first()).isEmpty()
    }

    @Test
    fun shouldRetrieveProject() = runTest {
        tagDao.insertAllTags(listOf(studyTag, designTag, sportTag))
        projectDao.insertAllProjects(listOf(project_1, project_2, project_4).map { it.asEntity() })
        assertThat(projectDao.getProject(project_1.id).first()).isNotNull()
    }

    @Test
    fun shouldReturnNullForProjectNotFound() = runTest {
        tagDao.insertAllTags(listOf(studyTag, designTag, sportTag))
        projectDao.insertAllProjects(listOf(project_1, project_2, project_4).map { it.asEntity() })
        assertThat(projectDao.getProject(project_3.id).first()).isNull()
    }
}