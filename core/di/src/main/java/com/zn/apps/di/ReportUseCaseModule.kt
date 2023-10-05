package com.zn.apps.di

import com.zn.apps.domain.UseCase
import com.zn.apps.domain.report.GetReportResourcesIntervalUseCase
import com.zn.apps.domain.report.GetReportResourcesUseCase
import com.zn.apps.domain.report.InsertReportUseCase
import com.zn.apps.domain.repository.ReportRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object ReportUseCaseModule {

    @Provides
    fun providesInsertReportUseCase(
        configuration: UseCase.Configuration,
        reportRepository: ReportRepository
    ): InsertReportUseCase = InsertReportUseCase(configuration, reportRepository)

    @Provides
    fun providesGetReportResourcesUseCase(
        configuration: UseCase.Configuration,
        reportRepository: ReportRepository
    ): GetReportResourcesUseCase = GetReportResourcesUseCase(configuration, reportRepository)

    @Provides
    fun providesGetReportResourcesIntervalUseCase(
        configuration: UseCase.Configuration,
        reportRepository: ReportRepository
    ): GetReportResourcesIntervalUseCase =
        GetReportResourcesIntervalUseCase(configuration, reportRepository)
}