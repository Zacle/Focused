package com.zn.apps.data_local.model

import com.zn.apps.data_local.database.task.TaskEntity
import com.zn.apps.model.data.task.Task

fun TaskEntity.asExternalModel() =
    Task(id, name, pomodoro, priority, projectId, tagId, dueDate, completedTime, completed)

fun Task.asEntity() =
    TaskEntity(id, name, pomodoro, priority, projectId, tagId, dueDate, completedTime, completed)