package com.zn.apps.data_local.model

import com.zn.apps.data_local.database.alarm.AlarmItemEntity
import com.zn.apps.model.data.alarm.AlarmItem

fun AlarmItem.asEntity() =
    AlarmItemEntity(taskId = task.id, remindAt = remindAt)