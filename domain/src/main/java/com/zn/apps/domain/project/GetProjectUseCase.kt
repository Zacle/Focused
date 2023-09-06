package com.zn.apps.domain.project

import com.zn.apps.domain.UseCase
import com.zn.apps.domain.repository.ProjectRepository
import com.zn.apps.model.data.project.Project
import com.zn.apps.model.usecase.UseCaseException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetProjectUseCase(
    configuration: Configuration,
    private val projectRepository: ProjectRepository
): UseCase<GetProjectUseCase.Request, GetProjectUseCase.Response>(configuration) {

    override suspend fun process(request: Request): Flow<Response> {
        return projectRepository.getProject(request.projectId)
            .map {
                if (it != null) {
                    Response(it)
                } else {
                    throw UseCaseException.ProjectNotFoundException(Throwable())
                }
            }
    }

    data class Request(val projectId: String): UseCase.Request

    data class Response(val project: Project): UseCase.Response
}