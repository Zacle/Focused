package com.zn.apps.di

import com.zn.apps.domain.UseCase
import com.zn.apps.domain.project.DeleteProjectUseCase
import com.zn.apps.domain.project.GetProjectResourcesUseCase
import com.zn.apps.domain.project.GetProjectUseCase
import com.zn.apps.domain.project.GetProjectsUseCase
import com.zn.apps.domain.project.UpsertProjectUseCase
import com.zn.apps.domain.repository.ProjectRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object ProjectUseCaseModule {

    @Provides
    fun providesDeleteProjectUseCase(
        configuration: UseCase.Configuration,
        projectRepository: ProjectRepository
    ): DeleteProjectUseCase = DeleteProjectUseCase(configuration, projectRepository)

    @Provides
    fun providesGetProjectResourcesUseCase(
        configuration: UseCase.Configuration,
        projectRepository: ProjectRepository
    ): GetProjectResourcesUseCase = GetProjectResourcesUseCase(configuration, projectRepository)

    @Provides
    fun providesGetProjectsUseCase(
        configuration: UseCase.Configuration,
        projectRepository: ProjectRepository
    ): GetProjectsUseCase = GetProjectsUseCase(configuration, projectRepository)

    @Provides
    fun providesGetProjectUseCase(
        configuration: UseCase.Configuration,
        projectRepository: ProjectRepository
    ): GetProjectUseCase = GetProjectUseCase(configuration, projectRepository)

    @Provides
    fun providesUpsertProjectUseCase(
        configuration: UseCase.Configuration,
        projectRepository: ProjectRepository
    ): UpsertProjectUseCase = UpsertProjectUseCase(configuration, projectRepository)
}