package com.zn.apps.data_repository.data_source.local

import com.zn.apps.model.data.alarm.AlarmItem
import kotlinx.coroutines.flow.Flow

interface AlarmDataSource {
    /**
     * Get all alarms from the database
     */
    fun getAlarms(): Flow<List<AlarmItem>>

    /**
     * Get the alarm based on the task's id
     */
    fun getAlarm(taskId: String): Flow<AlarmItem?>

    /**
     * Insert or modify the alarm
     */
    suspend fun upsertAlarm(alarmItem: AlarmItem)

    /**
     * Delete alarm
     */
    suspend fun deleteAlarm(alarmItem: AlarmItem)
}