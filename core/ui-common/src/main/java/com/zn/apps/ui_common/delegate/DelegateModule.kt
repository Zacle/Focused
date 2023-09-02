package com.zn.apps.ui_common.delegate

import com.zn.apps.common.network.di.ApplicationScope
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
        @ApplicationScope scope: CoroutineScope
    ): TasksViewModelDelegate =
        DefaultTasksViewModelDelegate(upsertTaskUseCase, deleteTaskUseCase, scope)
}