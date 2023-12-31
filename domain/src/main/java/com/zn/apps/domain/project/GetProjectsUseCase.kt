package com.zn.apps.domain.project

import com.zn.apps.domain.UseCase
import com.zn.apps.domain.repository.ProjectRepository
import com.zn.apps.model.data.project.Project
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetProjectsUseCase(
    configuration: Configuration,
    private val projectRepository: ProjectRepository
): UseCase<GetProjectsUseCase.Request, GetProjectsUseCase.Response>(configuration) {

    override suspend fun process(request: Request): Flow<Response> {
        return projectRepository.getProjects().map { Response(it) }
    }

    data object Request: UseCase.Request

    data class Response(val projects: List<Project>): UseCase.Response
}