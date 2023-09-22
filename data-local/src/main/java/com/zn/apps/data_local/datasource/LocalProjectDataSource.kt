package com.zn.apps.data_local.datasource

import com.zn.apps.data_local.database.project.ProjectDao
import com.zn.apps.data_local.database.task.PopulatedTaskEntity
import com.zn.apps.data_local.model.asEntity
import com.zn.apps.data_local.model.asExternalModel
import com.zn.apps.data_repository.data_source.local.ProjectDataSource
import com.zn.apps.model.data.project.PopulatedProjectResource
import com.zn.apps.model.data.project.Project
import com.zn.apps.model.data.project.ProjectResource
import com.zn.apps.model.data.task.TaskResource
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.mapLatest
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
class LocalProjectDataSource @Inject constructor(
    private val projectDao: ProjectDao
): ProjectDataSource {

    override fun getProjects(): Flow<List<Project>> {
        return projectDao.getProjects().mapLatest {
            it.map {  projectEntity ->
                projectEntity.asExternalModel()
            }
        }
    }

    override fun getProjectResources(): Flow<List<ProjectResource>> {
        return projectDao.getPopulatedProjects().mapLatest { populatedProjects ->
            populatedProjects.mapTo(mutableListOf()) { populatedProject ->
                val tasksCompleted = populatedProject.tasks.count { task ->
                    task.completed
                }
                val tagName =
                    if (populatedProject.tags.isEmpty()) "" else populatedProject.tags[0].name

                ProjectResource(
                    project = populatedProject.project.asExternalModel(),
                    numberOfTasks = populatedProject.tasks.size,
                    numberOfTasksCompleted = tasksCompleted,
                    tagName = tagName
                )
            }
        }
    }

    override fun getPopulatedProjectResource(projectId: String): Flow<PopulatedProjectResource?> {
        return projectDao.getPopulatedProjectWithTasks(projectId).mapLatest {
            if (it != null) {
                val tasks = it.taskResources.mapTo(mutableListOf()) { populatedTaskEntity ->
                    val (populatedProjectId, projectName, projectCompleted) = getProjectIdAndName(populatedTaskEntity)
                    val (tagId, tagName) = getTagIdAndName(populatedTaskEntity)
                    TaskResource(
                        task = populatedTaskEntity.task.asExternalModel(),
                        projectId = populatedProjectId,
                        projectName = projectName,
                        projectCompleted = projectCompleted,
                        tagId = tagId,
                        tagName = tagName
                    )
                }
                PopulatedProjectResource(
                    project = it.project.asExternalModel(),
                    taskResources = tasks
                )
            } else {
                null
            }
        }
    }

    private fun getProjectIdAndName(
        populatedTaskEntity: PopulatedTaskEntity
    ): Triple<String?, String, Boolean> {
        return if (populatedTaskEntity.project.isEmpty()) {
            Triple(null, "", false)
        }
        else {
            Triple(
                populatedTaskEntity.project[0].id,
                populatedTaskEntity.project[0].name,
                populatedTaskEntity.project[0].completed
            )
        }
    }

    private fun getTagIdAndName(populatedTaskEntity: PopulatedTaskEntity): Pair<String?, String> {
        return if (populatedTaskEntity.tag.isEmpty()) {
            Pair(null, "")
        }
        else {
            Pair(populatedTaskEntity.tag[0].id, populatedTaskEntity.tag[0].name)
        }
    }

    override fun getProject(projectId: String): Flow<Project?> {
        return projectDao.getProject(projectId).mapLatest { it?.asExternalModel() }
    }

    override suspend fun upsertProject(project: Project) {
        projectDao.upsertProject(project.asEntity())
    }

    override suspend fun deleteProject(project: Project) {
        projectDao.deleteProject(project.asEntity())
    }
}