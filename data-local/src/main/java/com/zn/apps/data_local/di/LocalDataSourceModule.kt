package com.zn.apps.data_local.di

import com.zn.apps.data_local.datasource.LocalAlarmDataSource
import com.zn.apps.data_local.datasource.LocalProjectDataSource
import com.zn.apps.data_local.datasource.LocalReportDataSource
import com.zn.apps.data_local.datasource.LocalTagDataSource
import com.zn.apps.data_local.datasource.LocalTaskDataSource
import com.zn.apps.data_repository.data_source.local.AlarmDataSource
import com.zn.apps.data_repository.data_source.local.ProjectDataSource
import com.zn.apps.data_repository.data_source.local.ReportDataSource
import com.zn.apps.data_repository.data_source.local.TagDataSource
import com.zn.apps.data_repository.data_source.local.TaskDataSource
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class LocalDataSourceModule {

    @Binds
    abstract fun bindsTaskDataSource(localTaskDataSource: LocalTaskDataSource): TaskDataSource

    @Binds
    abstract fun bindsTagDataSource(localTagDataSource: LocalTagDataSource): TagDataSource

    @Binds
    abstract fun bindsProjectDataSource(localProjectDataSource: LocalProjectDataSource): ProjectDataSource

    @Binds
    abstract fun bindsReportDataSource(localReportDataSource: LocalReportDataSource): ReportDataSource

    @Binds
    abstract fun bindsAlarmDataSource(localAlarmDataSource: LocalAlarmDataSource): AlarmDataSource
}