package com.zn.apps.core.notification

import android.Manifest
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.zn.apps.ui_design.icon.FAIcons
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TaskAlarmNotifier @Inject constructor(
    @ApplicationContext private val context: Context
): Notifier {
    private val notificationManager = NotificationManagerCompat.from(context)

    override fun createBaseNotification(
        channelId: String,
        block: NotificationCompat.Builder.() -> Unit
    ): Notification {
        ensureNotificationChannelsExist()
        return NotificationCompat.Builder(context, channelId)
            .setSmallIcon(FAIcons.task)
            .setSilent(false)
            .setContentIntent(taskPendingIntent())
            .setAutoCancel(true)
            .apply(block)
            .build()
    }

    fun showTaskReminder(title: String) = with(context) {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        val notification = createBaseNotification(TASK_ALARM_NOTIFICATION_CHANNEL_ID) {
            setContentTitle(getString(R.string.task_alarm_notification_channel_description))
            setContentText(getString(R.string.task_alarm_notification_text) + " " + title)
            setStyle(
                NotificationCompat
                    .BigTextStyle()
                    .bigText(getString(R.string.task_alarm_notification_text) + " " + title)
            )
            priority = NotificationCompat.PRIORITY_DEFAULT
        }
        notificationManager.notify(TASK_ALARM_NOTIFICATION_ID, notification)
    }

    override fun ensureNotificationChannelsExist() {
        val channel = NotificationChannel(
            TASK_ALARM_NOTIFICATION_CHANNEL_ID,
            context.getString(R.string.task_alarm_notification_channel_name),
            NotificationManager.IMPORTANCE_DEFAULT
        ).apply {
            description = context.getString(R.string.task_alarm_notification_channel_description)
        }

        NotificationManagerCompat.from(context).createNotificationChannel(channel)
    }

    private fun taskPendingIntent(): PendingIntent? =
        PendingIntent.getActivity(
            context,
            CLICK_REQUEST_CODE,
            Intent().apply {
                action = Intent.ACTION_VIEW
                component = ComponentName(
                    context.packageName,
                    TARGET_ACTIVITY_NAME
                )
            },
            PendingIntent.FLAG_IMMUTABLE
        )
}

private const val TASK_ALARM_NOTIFICATION_CHANNEL_ID = "TASK_ALARM_NOTIFICATION_CHANNEL_ID"
private const val TASK_ALARM_NOTIFICATION_ID = 11
private const val CLICK_REQUEST_CODE = 10
private const val TARGET_ACTIVITY_NAME = "com.zn.apps.focused.MainActivity"