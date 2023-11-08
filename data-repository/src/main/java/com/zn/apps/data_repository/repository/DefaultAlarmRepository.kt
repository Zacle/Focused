package com.zn.apps.data_repository.repository

import com.zn.apps.data_repository.data_source.local.AlarmDataSource
import com.zn.apps.domain.repository.AlarmRepository
import com.zn.apps.model.data.alarm.AlarmItem
import kotlinx.coroutines.flow.Flow

class DefaultAlarmRepository(
    private val alarmDataSource: AlarmDataSource
): AlarmRepository {

    override fun getAlarms(): Flow<List<AlarmItem>> = alarmDataSource.getAlarms()

    override fun getAlarm(taskId: String): Flow<AlarmItem?> = alarmDataSource.getAlarm(taskId)

    override suspend fun upsertAlarm(alarmItem: AlarmItem) {
        alarmDataSource.upsertAlarm(alarmItem)
    }

    override suspend fun deleteAlarm(taskId: String) {
        alarmDataSource.deleteAlarm(taskId)
    }
}