package com.zn.apps.data_local.model

import com.zn.apps.data_local.database.task.TaskEntity
import com.zn.apps.model.data.task.Task

fun TaskEntity.asExternalModel() =
    Task(
        id = id,
        name = name,
        pomodoro = pomodoro,
        priority = priority,
        projectId = projectId,
        tagId = tagId,
        remindTaskAt = remindTaskAt,
        shouldRemindTask = shouldRemindTask,
        dueDate = dueDate,
        completedTime = completedTime,
        completed = completed
    )

fun Task.asEntity() =
    TaskEntity(
        id = id,
        name = name,
        pomodoro = pomodoro,
        priority = priority,
        projectId = projectId,
        tagId = tagId,
        remindTaskAt = remindTaskAt,
        shouldRemindTask = shouldRemindTask,
        dueDate = dueDate,
        completedTime = completedTime,
        completed = completed
    )