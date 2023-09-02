package com.zn.apps.di

import com.zn.apps.data_repository.data_source.local.TagDataSource
import com.zn.apps.data_repository.data_source.local.TaskDataSource
import com.zn.apps.data_repository.repository.DefaultTagRepository
import com.zn.apps.data_repository.repository.DefaultTaskRepository
import com.zn.apps.data_repository.repository.DefaultUserDataRepository
import com.zn.apps.datastore.FocusedUserPreferencesDataSource
import com.zn.apps.domain.repository.TagRepository
import com.zn.apps.domain.repository.TaskRepository
import com.zn.apps.domain.repository.UserDataRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent


@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    fun providesTaskRepository(
        taskDataSource: TaskDataSource
    ): TaskRepository = DefaultTaskRepository(taskDataSource)

    @Provides
    fun providesUserDataRepository(
        focusedUserPreferencesDataSource: FocusedUserPreferencesDataSource
    ): UserDataRepository = DefaultUserDataRepository(focusedUserPreferencesDataSource)

    @Provides
    fun providesTagRepository(
        tagDataSource: TagDataSource
    ): TagRepository = DefaultTagRepository(tagDataSource)
}