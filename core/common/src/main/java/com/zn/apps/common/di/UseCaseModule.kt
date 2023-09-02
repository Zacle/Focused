package com.zn.apps.common.di

import com.zn.apps.common.network.Dispatcher
import com.zn.apps.common.network.FocusedDispatchers.IO
import com.zn.apps.domain.UseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher

@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {

    @Provides
    fun providesUseCaseConfiguration(
        @Dispatcher(IO) dispatcher: CoroutineDispatcher
    ): UseCase.Configuration = UseCase.Configuration(dispatcher)

}