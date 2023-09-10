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
     * Get projects, do not embed tasks and tags
     */
    fun getProjects(): Flow<List<Project>>

    /**
     * Get the project that matches the project id
     *
     * @param projectId the id of the project to be retrieved
     */
    fun getProject(projectId: String): Flow<Project?>

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