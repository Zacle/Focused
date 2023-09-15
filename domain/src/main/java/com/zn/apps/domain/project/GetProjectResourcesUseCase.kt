package com.zn.apps.domain.project

import com.zn.apps.domain.UseCase
import com.zn.apps.domain.repository.ProjectRepository
import com.zn.apps.model.data.project.ProjectFilterType.ALL
import com.zn.apps.model.data.project.ProjectFilterType.COMPLETED
import com.zn.apps.model.data.project.ProjectFilterType.ONGOING
import com.zn.apps.model.data.project.ProjectFiltration
import com.zn.apps.model.data.project.ProjectResource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetProjectResourcesUseCase(
    configuration: Configuration,
    private val projectRepository: ProjectRepository
): UseCase<GetProjectResourcesUseCase.Request, GetProjectResourcesUseCase.Response>(configuration) {

    override suspend fun process(request: Request): Flow<Response> {
        return projectRepository.getProjectResources().map {
            Response(filter(request.filter, it))
        }
    }

    private fun filter(
        filter: ProjectFiltration,
        projectResources: List<ProjectResource>
    ): List<ProjectResource> {
        val projects = projectResources.filter { projectResource ->
            when (filter.filterType) {
                ONGOING -> !projectResource.project.completed
                COMPLETED -> projectResource.project.completed
                ALL -> true
            }
        }
        return projects.filter { it.project.name.contains(filter.query, ignoreCase = true) }
    }

    data class Request(val filter: ProjectFiltration): UseCase.Request

    data class Response(val projectResources: List<ProjectResource>): UseCase.Response
}