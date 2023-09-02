package com.zn.apps.model.data.task

import java.time.OffsetDateTime
import java.util.UUID

data class Task(
    val id: String = UUID.randomUUID().toString(),
    val name: String,
    val pomodoro: Pomodoro = Pomodoro(),
    val priority: TaskPriority = TaskPriority.NONE,
    val projectId: String? = null,
    val tagId: String? = null,
    val dueDate: OffsetDateTime? = null,
    val completedTime: OffsetDateTime? = null,
    val completed: Boolean = false
)