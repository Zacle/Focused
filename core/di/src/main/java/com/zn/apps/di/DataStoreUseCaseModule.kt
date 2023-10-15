package com.zn.apps.di

import com.zn.apps.domain.UseCase
import com.zn.apps.domain.datastore.GetPomodoroPreferencesUseCase
import com.zn.apps.domain.datastore.GetUserDataUseCase
import com.zn.apps.domain.datastore.user_data.SetShouldHideOnboardingUseCase
import com.zn.apps.domain.repository.PomodoroPreferencesRepository
import com.zn.apps.domain.repository.UserDataRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent


@Module
@InstallIn(SingletonComponent::class)
object DataStoreUseCaseModule {

    @Provides
    fun providesSetShouldHideOnboardingUseCase(
        configuration: UseCase.Configuration,
        userDataRepository: UserDataRepository
    ): SetShouldHideOnboardingUseCase =
        SetShouldHideOnboardingUseCase(configuration, userDataRepository)

    @Provides
    fun providesGetUserDataUseCase(
        configuration: UseCase.Configuration,
        userDataRepository: UserDataRepository
    ): GetUserDataUseCase = GetUserDataUseCase(configuration, userDataRepository)

    @Provides
    fun providesGetPomodoroPreferencesUseCase(
        configuration: UseCase.Configuration,
        pomodoroPreferencesRepository: PomodoroPreferencesRepository
    ): GetPomodoroPreferencesUseCase =
        GetPomodoroPreferencesUseCase(configuration, pomodoroPreferencesRepository)
}