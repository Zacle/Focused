package com.zn.apps.data_local.datasource

import com.zn.apps.data_local.database.alarm.AlarmDao
import com.zn.apps.data_local.model.asEntity
import com.zn.apps.data_local.model.asExternalModel
import com.zn.apps.data_repository.data_source.local.AlarmDataSource
import com.zn.apps.model.data.alarm.AlarmItem
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.mapLatest
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
class LocalAlarmDataSource @Inject constructor(
    private val alarmDao: AlarmDao
): AlarmDataSource {

    override fun getAlarms(): Flow<List<AlarmItem>> =
        alarmDao.getAlarms().mapLatest {
            val alarmItems = mutableListOf<AlarmItem>()
            it.forEach { populatedAlarmItemEntity ->
                if (populatedAlarmItemEntity.task.isNotEmpty()) {
                    alarmItems.add(
                        AlarmItem(
                            task = populatedAlarmItemEntity.task[0].asExternalModel(),
                            remindAt = populatedAlarmItemEntity.alarmItem.remindAt
                        )
                    )
                }
            }
            alarmItems
        }

    override fun getAlarm(taskId: String): Flow<AlarmItem?> =
        alarmDao.getAlarm(taskId).mapLatest {
            var alarmItem: AlarmItem? = null
            it?.let { populatedAlarmItemEntity ->
                if (populatedAlarmItemEntity.task.isNotEmpty())
                    alarmItem = AlarmItem(
                        task = populatedAlarmItemEntity.task[0].asExternalModel(),
                        remindAt = populatedAlarmItemEntity.alarmItem.remindAt
                    )
            }
            alarmItem
        }

    override suspend fun upsertAlarm(alarmItem: AlarmItem) {
        alarmDao.upsertAlarm(alarmItem.asEntity())
    }

    override suspend fun deleteAlarm(taskId: String) {
        alarmDao.deleteAlarm(taskId)
    }
}