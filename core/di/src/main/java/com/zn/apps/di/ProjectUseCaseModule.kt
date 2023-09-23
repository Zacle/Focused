package com.zn.apps.di

import android.content.Context
import com.zn.apps.domain.UseCase
import com.zn.apps.domain.project.DeleteProjectUseCase
import com.zn.apps.domain.project.GetPopulatedProjectResourceUseCase
import com.zn.apps.domain.project.GetProjectMetadataUseCase
import com.zn.apps.domain.project.GetProjectResourcesUseCase
import com.zn.apps.domain.project.GetProjectTasksWithMetadata
import com.zn.apps.domain.project.GetProjectUseCase
import com.zn.apps.domain.project.GetProjectsUseCase
import com.zn.apps.domain.project.UpsertProjectUseCase
import com.zn.apps.domain.repository.ProjectRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
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
    fun providesGetPopulatedProjectResourceUseCase(
        configuration: UseCase.Configuration,
        @ApplicationContext context: Context,
        projectRepository: ProjectRepository
    ): GetPopulatedProjectResourceUseCase =
        GetPopulatedProjectResourceUseCase(configuration, context, projectRepository)

    @Provides
    fun providesGetProjectMetadataUseCase(
        configuration: UseCase.Configuration,
        projectRepository: ProjectRepository
    ): GetProjectMetadataUseCase = GetProjectMetadataUseCase(configuration, projectRepository)

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
    fun providesGetProjectTasksWithMetadata(
        configuration: UseCase.Configuration,
        getProjectMetadataUseCase: GetProjectMetadataUseCase,
        getPopulatedProjectResourceUseCase: GetPopulatedProjectResourceUseCase,
        getProjectUseCase: GetProjectUseCase
    ): GetProjectTasksWithMetadata =
        GetProjectTasksWithMetadata(
            configuration = configuration,
            getProjectMetadataUseCase = getProjectMetadataUseCase,
            getPopulatedProjectResourceUseCase = getPopulatedProjectResourceUseCase,
            getProjectUseCase = getProjectUseCase
        )

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