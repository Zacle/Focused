package com.zn.apps.data_local.datasource

import com.zn.apps.data_local.database.project.ProjectDao
import com.zn.apps.data_local.model.asEntity
import com.zn.apps.data_local.model.asExternalModel
import com.zn.apps.data_repository.data_source.local.ProjectDataSource
import com.zn.apps.model.data.project.Project
import com.zn.apps.model.data.project.ProjectResource
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.mapLatest

@OptIn(ExperimentalCoroutinesApi::class)
class LocalProjectDataSource(
    private val projectDao: ProjectDao
): ProjectDataSource {

    override fun getProjectResources(): Flow<List<ProjectResource>> {
        return projectDao.getProjects().mapLatest { populatedProjects ->
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