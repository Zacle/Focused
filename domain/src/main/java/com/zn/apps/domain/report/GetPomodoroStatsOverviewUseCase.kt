package com.zn.apps.domain.report

import com.zn.apps.common.millisecondsToHours
import com.zn.apps.domain.UseCase
import com.zn.apps.domain.repository.ReportRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetPomodoroStatsOverviewUseCase(
    configuration: Configuration,
    private val reportRepository: ReportRepository
): UseCase<GetPomodoroStatsOverviewUseCase.Request, GetPomodoroStatsOverviewUseCase.Response>(configuration) {

    override suspend fun process(request: Request): Flow<Response> =
        reportRepository.getReportResources().map { reportResources ->
            var totalHours: Long = 0
            val totalPomodoro = reportResources.size
            reportResources.forEach { reportResource ->
                totalHours += reportResource.elapsedTime
            }
            Response(
                totalHours = totalHours.millisecondsToHours(),
                pomodoroCompleted = totalPomodoro
            )
        }

    data object Request: UseCase.Request

    data class Response(
        val totalHours: Int,
        val pomodoroCompleted: Int
    ): UseCase.Response
}