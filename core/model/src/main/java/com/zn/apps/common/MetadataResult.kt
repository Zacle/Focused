package com.zn.apps.common

import com.zn.apps.model.data.task.TaskTime

data class MetadataResult(
    val totalTaskTime: TaskTime,
    val estimatedTaskTime: TaskTime,
    val elapsedTaskTime: TaskTime,
    val totalTime: Long,
    val estimatedTime: Long,
    val elapsedTime: Long,
    val totalTasks: Int,
    val tasksToBeCompleted: Int,
    val tasksCompleted: Int,
    val metadataType: MetadataType
) {
    companion object {
        val initialMetadataResult = MetadataResult(
            totalTaskTime = TaskTime(0, 0),
            estimatedTaskTime = TaskTime(0, 0),
            elapsedTaskTime = TaskTime(0, 0),
            totalTime = 0L,
            estimatedTime = 0L,
            elapsedTime = 0L,
            totalTasks = 0,
            tasksToBeCompleted = 0,
            tasksCompleted = 0,
            metadataType = MetadataType.SHOW
        )
    }
}

enum class MetadataType {
    SHOW,
    HIDE
}

fun metadataFromTimeType(timeType: DeadlineType): MetadataType =
    when(timeType) {
        DeadlineType.TODAY -> MetadataType.SHOW
        else -> MetadataType.HIDE
    }