package com.zn.apps.common.alarm

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import dagger.hilt.android.qualifiers.ApplicationContext
import java.time.OffsetDateTime
import javax.inject.Inject

private const val DAILY_TODO_REMINDER = 777

class DailyTodoReminderAlarmScheduler @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val alarmManager = context.getSystemService(AlarmManager::class.java)

    fun scheduleAlarm() {
        val intent = Intent(context, DailyTodoReminderAlarmReceiver::class.java)
        val time = OffsetDateTime
            .now()
            .plusDays(1)
            .withHour(9)
            .withMinute(0)
            .withSecond(0)

        alarmManager.setRepeating(
            AlarmManager.RTC_WAKEUP,
            time.toInstant().toEpochMilli(),
            AlarmManager.INTERVAL_DAY,
            PendingIntent.getBroadcast(
                context,
                DAILY_TODO_REMINDER,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
        )
    }

    fun cancel() {
        alarmManager.cancel(
            PendingIntent.getBroadcast(
                context,
                DAILY_TODO_REMINDER,
                Intent(context, DailyTodoReminderAlarmReceiver::class.java),
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
        )
    }
}