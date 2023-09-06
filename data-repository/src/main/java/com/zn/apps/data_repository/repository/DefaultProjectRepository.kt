package com.zn.apps.data_repository.repository

import com.zn.apps.data_repository.data_source.local.ProjectDataSource
import com.zn.apps.domain.repository.ProjectRepository
import com.zn.apps.model.data.project.Project
import com.zn.apps.model.data.project.ProjectResource
import kotlinx.coroutines.flow.Flow

class DefaultProjectRepository(
    private val projectDataSource: ProjectDataSource
): ProjectRepository {

    override fun getProjectResources(): Flow<List<ProjectResource>> {
        return projectDataSource.getProjectResources()
    }

    override fun getProject(projectId: String): Flow<Project?> {
        return projectDataSource.getProject(projectId)
    }

    override suspend fun upsertProject(project: Project) {
        projectDataSource.upsertProject(project)
    }

    override suspend fun deleteProject(project: Project) {
        projectDataSource.deleteProject(project)
    }
}