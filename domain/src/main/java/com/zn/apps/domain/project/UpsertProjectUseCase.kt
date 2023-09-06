package com.zn.apps.domain.project

import android.database.sqlite.SQLiteException
import com.zn.apps.domain.UseCase
import com.zn.apps.domain.repository.ProjectRepository
import com.zn.apps.model.data.project.Project
import com.zn.apps.model.usecase.UseCaseException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class UpsertProjectUseCase(
    configuration: Configuration,
    private val projectRepository: ProjectRepository
): UseCase<UpsertProjectUseCase.Request, UpsertProjectUseCase.Response>(configuration) {

    override suspend fun process(request: Request): Flow<Response> {
        val response = try {
            projectRepository.upsertProject(request.project)
            Response
        } catch (exception: SQLiteException) {
            throw UseCaseException.ProjectNotUpdatedException(exception)
        }
        return flowOf(response)
    }

    data class Request(val project: Project): UseCase.Request

    data object Response: UseCase.Response
}