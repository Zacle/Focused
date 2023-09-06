package com.zn.apps.data_repository.data_source.local

import com.zn.apps.model.data.project.Project
import com.zn.apps.model.data.project.ProjectResource
import kotlinx.coroutines.flow.Flow

/**
 * Main entry point for accessing the projects in the database: either
 * local or remote
 */
interface ProjectDataSource {

    /**
     * Get all projects
     */
    fun getProjectResources(): Flow<List<ProjectResource>>

    /**
     * Get a project that match the given id or returns null
     *
     * @param projectId
     */
    fun getProject(projectId: String): Flow<Project?>

    /**
     * Add or modify the project
     *
     * @param project
     */
    suspend fun upsertProject(project: Project)

    /**
     * Delete the project
     *
     * @param project
     */
    suspend fun deleteProject(project: Project)
}