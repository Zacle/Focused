package com.zn.apps.model.data.alarm

import com.zn.apps.model.data.task.Task
import java.time.OffsetDateTime

data class AlarmItem(
    val task: Task,
    val remindAt: OffsetDateTime
)
