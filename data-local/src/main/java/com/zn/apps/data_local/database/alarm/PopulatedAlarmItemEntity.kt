package com.zn.apps.data_local.database.alarm

import androidx.room.Embedded
import androidx.room.Relation
import com.zn.apps.data_local.database.task.TaskEntity

data class PopulatedAlarmItemEntity(
    @Embedded val alarmItem: AlarmItemEntity,
    @Relation(
        parentColumn = "task_id",
        entityColumn = "id"
    )
    val task: List<TaskEntity>
)
