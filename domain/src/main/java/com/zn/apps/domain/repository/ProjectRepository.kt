package com.zn.apps.domain.repository

import com.zn.apps.model.data.project.Project
import com.zn.apps.model.data.project.ProjectResource
import kotlinx.coroutines.flow.Flow

interface ProjectRepository {

    /**
     * Get project resources. Project Resources are embedded with tasks and tag's name
     */
    fun getProjectResources(): Flow<List<ProjectResource>>

    /**
     * Get the project resource that matches the project id
     *
     * @param projectId the id of the project to be retrieved
     */
    fun getProjectResource(projectId: String): Flow<ProjectResource?>

    /**
     * Update the project if it already exists or insert a new project
     *
     * @param project
     */
    suspend fun upsertProject(project: Project)

    /**
     * Delete project from the database
     *
     * @param project
     */
    suspend fun deleteProject(project: Project)
}