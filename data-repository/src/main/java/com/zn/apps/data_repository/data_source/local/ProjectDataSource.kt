package com.zn.apps.data_repository.data_source.local

import com.zn.apps.model.data.project.PopulatedProjectResource
import com.zn.apps.model.data.project.Project
import com.zn.apps.model.data.project.ProjectResource
import kotlinx.coroutines.flow.Flow

/**
 * Main entry point for accessing the projects in the database: either
 * local or remote
 */
interface ProjectDataSource {

    /**
     * Get populated project resource. Convert data from [PopulatedProjectEntity] to a
     * [PopulatedProjectResource] model to be presented to the UI
     */
    fun getPopulatedProjectResource(projectId: String): Flow<PopulatedProjectResource?>

    /**
     * Get all projects. Convert data from [PopulatedProjectEntity] to a [ProjectResource] model
     * to be presented to the UI
     */
    fun getProjectResources(): Flow<List<ProjectResource>>

    /**
     * Get all projects and do not populate them with tags and tasks
     */
    fun getProjects(): Flow<List<Project>>

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