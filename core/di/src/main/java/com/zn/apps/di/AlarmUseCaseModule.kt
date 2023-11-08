package com.zn.apps.di

import com.zn.apps.domain.UseCase
import com.zn.apps.domain.alarm.DeleteAlarmUseCase
import com.zn.apps.domain.alarm.GetAlarmUseCase
import com.zn.apps.domain.alarm.GetAlarmsUseCase
import com.zn.apps.domain.alarm.UpsertAlarmUseCase
import com.zn.apps.domain.repository.AlarmRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object AlarmUseCaseModule {

    @Provides
    fun providesDeleteAlarmUseCase(
        configuration: UseCase.Configuration,
        alarmRepository: AlarmRepository
    ): DeleteAlarmUseCase = DeleteAlarmUseCase(configuration, alarmRepository)

    @Provides
    fun providesGetAlarmsUseCase(
        configuration: UseCase.Configuration,
        alarmRepository: AlarmRepository
    ): GetAlarmsUseCase = GetAlarmsUseCase(configuration, alarmRepository)

    @Provides
    fun providesGetAlarmUseCase(
        configuration: UseCase.Configuration,
        alarmRepository: AlarmRepository
    ): GetAlarmUseCase = GetAlarmUseCase(configuration, alarmRepository)

    @Provides
    fun providesUpsertAlarmUseCase(
        configuration: UseCase.Configuration,
        alarmRepository: AlarmRepository
    ): UpsertAlarmUseCase = UpsertAlarmUseCase(configuration, alarmRepository)
}