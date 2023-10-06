package com.zn.apps.domain.task

import com.zn.apps.common.DeadlineTimeHelper
import com.zn.apps.common.MetadataResult
import com.zn.apps.common.metadataFromTimeType
import com.zn.apps.domain.UseCase
import com.zn.apps.domain.repository.ReportRepository
import com.zn.apps.domain.repository.TaskRepository
import com.zn.apps.domain.util.filtering.Filtering
import com.zn.apps.domain.util.formatToString
import com.zn.apps.filter.Filter
import com.zn.apps.model.data.report.ReportResource
import com.zn.apps.model.data.task.TaskResource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import java.time.OffsetDateTime

class GetTasksMetadataUseCase(
    configuration: Configuration,
    private val taskRepository: TaskRepository,
    private val reportRepository: ReportRepository
): UseCase<GetTasksMetadataUseCase.Request, GetTasksMetadataUseCase.Response>(configuration) {

    override suspend fun process(request: Request): Flow<Response> {
        val filter = request.filter
        val interval = getInterval()
        return combine(
            taskRepository.getTasks(),
            reportRepository.getReportResources(
                from = interval.first.formatToString(),
                to = interval.second.formatToString()
            )
        ) { taskResources, reportResources ->
            val filteringStrategy = Filtering.obtainStrategy(filter, taskResources.filterCompletedProject(true))
            val filteredTaskResources = filteringStrategy.filter(filter)
            val metadataResult = computeMetadata(filteredTaskResources, reportResources, filter, interval)
            Response(metadataResult)
        }
    }

    private fun computeMetadata(
        taskResources: List<TaskResource>,
        reportResources: List<ReportResource>,
        filter: Filter.DateFilter,
        interval: Pair<OffsetDateTime, OffsetDateTime>
    ): MetadataResult {
        var totalTime = 0L
        var estimatedTime = 0L
        var elapsedTime = 0L
        var tasksToBeCompleted = 0
        var tasksCompleted = 0
        taskResources.forEach { taskResource ->
            val pomodoro = taskResource.task.pomodoro
            totalTime += pomodoro.pomodoroNumber * pomodoro.pomodoroLength
            estimatedTime +=
                if (!taskResource.task.completed) pomodoro.getEstimatedRemainingTime() else 0
            if (!taskResource.task.completed)
                tasksToBeCompleted += 1
            else {
                if (taskResource.task.completedTime != null) {
                    val completedTime = taskResource.task.completedTime!!
                    if (completedTime >= interval.first && completedTime <= interval.second)
                        tasksCompleted += 1
                }
            }
        }
        reportResources.forEach { reportResource ->
            elapsedTime += reportResource.elapsedTime
        }
        return MetadataResult(
            totalTaskTime = DeadlineTimeHelper.getTaskTime(totalTime),
            estimatedTaskTime = DeadlineTimeHelper.getTaskTime(estimatedTime),
            elapsedTaskTime = DeadlineTimeHelper.getTaskTime(elapsedTime),
            totalTime = totalTime,
            estimatedTime = estimatedTime,
            elapsedTime = elapsedTime,
            totalTasks = taskResources.size,
            tasksToBeCompleted = tasksToBeCompleted,
            tasksCompleted = tasksCompleted,
            metadataType = metadataFromTimeType(filter.dueDate)
        )
    }

    private fun getInterval(): Pair<OffsetDateTime, OffsetDateTime> {
        /** If the date filter is today, the start interval is today at midnight */
        val from = OffsetDateTime.now().withHour(0).withMinute(0).withSecond(0)
        /** the end interval is today at 23:59 **/
        val to = OffsetDateTime.now().withHour(23).withMinute(59)
        return Pair(from, to)
    }

    data class Request(
        val filter: Filter.DateFilter
    ): UseCase.Request

    data class Response(val metadataResult: MetadataResult): UseCase.Response
}