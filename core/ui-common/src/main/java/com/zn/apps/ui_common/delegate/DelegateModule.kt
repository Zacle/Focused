package com.zn.apps.ui_common.delegate

import com.zn.apps.common.network.di.ApplicationScope
import com.zn.apps.domain.project.DeleteProjectUseCase
import com.zn.apps.domain.project.GetProjectsUseCase
import com.zn.apps.domain.project.UpsertProjectUseCase
import com.zn.apps.domain.tag.GetTagsUseCase
import com.zn.apps.domain.task.DeleteTaskUseCase
import com.zn.apps.domain.task.UpsertTaskUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope

@Module
@InstallIn(SingletonComponent::class)
object DelegateModule {

    @Provides
    fun providesTasksViewModelDelegate(
        upsertTaskUseCase: UpsertTaskUseCase,
        deleteTaskUseCase: DeleteTaskUseCase,
        getProjectsUseCase: GetProjectsUseCase,
        getTagsUseCase: GetTagsUseCase,
        @ApplicationScope scope: CoroutineScope
    ): TasksViewModelDelegate =
        DefaultTasksViewModelDelegate(
            upsertTaskUseCase = upsertTaskUseCase,
            deleteTaskUseCase = deleteTaskUseCase,
            getTagsUseCase = getTagsUseCase,
            getProjectsUseCase = getProjectsUseCase,
            scope = scope
        )

    @Provides
    fun providesProjectsViewModelDelegate(
        upsertProjectUseCase: UpsertProjectUseCase,
        deleteProjectUseCase: DeleteProjectUseCase,
        getTagsUseCase: GetTagsUseCase,
        @ApplicationScope scope: CoroutineScope
    ): ProjectsViewModelDelegate =
        DefaultProjectsViewModelDelegate(
            upsertProjectUseCase = upsertProjectUseCase,
            deleteProjectUseCase = deleteProjectUseCase,
            getTagsUseCase = getTagsUseCase,
            scope = scope
        )
}