package com.zn.apps.di

import android.content.Context
import com.zn.apps.domain.GetTasksWithTagsUseCase
import com.zn.apps.domain.UseCase
import com.zn.apps.domain.repository.TaskRepository
import com.zn.apps.domain.tag.GetTagsUseCase
import com.zn.apps.domain.task.DeleteTaskUseCase
import com.zn.apps.domain.task.GetTaskUseCase
import com.zn.apps.domain.task.GetTasksUseCase
import com.zn.apps.domain.task.UpsertTaskUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent


@Module
@InstallIn(SingletonComponent::class)
object TaskUseCaseModule {

    @Provides
    fun providesDeleteTaskUseCase(
        configuration: UseCase.Configuration,
        taskRepository: TaskRepository
    ): DeleteTaskUseCase = DeleteTaskUseCase(configuration, taskRepository)

    @Provides
    fun providesGetTasksUseCase(
        configuration: UseCase.Configuration,
        @ApplicationContext context: Context,
        taskRepository: TaskRepository
    ): GetTasksUseCase = GetTasksUseCase(configuration, context, taskRepository)

    @Provides
    fun providesGetTaskUseCase(
        configuration: UseCase.Configuration,
        taskRepository: TaskRepository
    ): GetTaskUseCase = GetTaskUseCase(configuration, taskRepository)

    @Provides
    fun providesUpsertTaskUseCase(
        configuration: UseCase.Configuration,
        taskRepository: TaskRepository
    ): UpsertTaskUseCase = UpsertTaskUseCase(configuration, taskRepository)

    @Provides
    fun providesGetTasksWithTagsUseCase(
        configuration: UseCase.Configuration,
        tasksUseCase: GetTasksUseCase,
        tagsUseCase: GetTagsUseCase
    ): GetTasksWithTagsUseCase = GetTasksWithTagsUseCase(configuration, tasksUseCase, tagsUseCase)
}