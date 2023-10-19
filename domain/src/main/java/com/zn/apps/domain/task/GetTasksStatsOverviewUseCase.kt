package com.zn.apps.domain.task

import com.zn.apps.common.millisecondsToHours
import com.zn.apps.domain.UseCase
import com.zn.apps.domain.repository.ReportRepository
import com.zn.apps.domain.repository.TaskRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

class GetTasksStatsOverviewUseCase(
    configuration: Configuration,
    private val reportRepository: ReportRepository,
    private val taskRepository: TaskRepository
): UseCase<GetTasksStatsOverviewUseCase.Request, GetTasksStatsOverviewUseCase.Response>(configuration) {

    override suspend fun process(request: Request): Flow<Response> =
        combine(
            reportRepository.getReportResources(),
            taskRepository.getTaskResources(true)
        ) { reportResources, taskResources ->
            var totalHours: Long = 0
            val totalTasks = taskResources.size
            reportResources.forEach { reportResource ->
                if (reportResource.task != null) {
                    totalHours += reportResource.elapsedTime
                }
            }
            Response(
                totalHoursSpent = totalHours.millisecondsToHours(),
                tasksCompleted = totalTasks
            )
        }

    data object Request: UseCase.Request

    data class Response(
        val totalHoursSpent: Int,
        val tasksCompleted: Int
    ): UseCase.Response
}