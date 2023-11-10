package com.zn.apps.common.alarm

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.zn.apps.common.alarm.AlarmConstants.TASK_ID
import com.zn.apps.common.alarm.AlarmConstants.TASK_NAME
import com.zn.apps.model.data.alarm.AlarmItem
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class TaskAlarmScheduler @Inject constructor(
    @ApplicationContext private val context: Context
): AlarmScheduler {
    private val alarmManager = context.getSystemService(AlarmManager::class.java) as AlarmManager

    override fun scheduleAlarm(alarmItem: AlarmItem) {
        val intent = Intent(context, TaskAlarmReceiver::class.java).apply {
            putExtra(TASK_ID, alarmItem.task.id)
            putExtra(TASK_NAME, alarmItem.task.name)
        }
        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            alarmItem.remindAt.toInstant().toEpochMilli(),
            PendingIntent.getBroadcast(
                context,
                alarmItem.task.id.hashCode(),
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
        )
    }

    override fun cancelAlarm(alarmItem: AlarmItem) {
        alarmManager.cancel(
            PendingIntent.getBroadcast(
                context,
                alarmItem.task.id.hashCode(),
                Intent(context, TaskAlarmReceiver::class.java),
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
        )
    }
}