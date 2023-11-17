package com.zn.apps.sync.workers.utils

import android.app.Notification
import androidx.work.ForegroundInfo

private const val SYNC_ALARM_NOTIFICATION_ID = 13

fun syncAlarmForegroundInfo(notification: () -> Notification) = ForegroundInfo(
    SYNC_ALARM_NOTIFICATION_ID,
    notification()
)