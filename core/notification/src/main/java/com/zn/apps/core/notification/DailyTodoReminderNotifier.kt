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
class DailyTodoReminderNotifier @Inject constructor(
    @ApplicationContext private val context: Context
): Notifier {
    private val notificationManager = NotificationManagerCompat.from(context)

    override fun createBaseNotification(
        channelId: String,
        block: NotificationCompat.Builder.() -> Unit
    ): Notification {
        ensureNotificationChannelsExist()
        return NotificationCompat.Builder(context, channelId)
            .setSmallIcon(FAIcons.notification)
            .setSilent(false)
            .setContentIntent(dailyTodoPendingIntent())
            .setAutoCancel(true)
            .apply(block)
            .build()
    }

    fun showDailyTodoReminder() = with(context) {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        val notification = createBaseNotification(DAILY_TODO_REMINDER_CHANNEL_ID) {
            setContentTitle(getString(R.string.daily_todo_reminder_notification_text))
            setContentText(getString(R.string.daily_todo_reminder_notification_description))
            priority = NotificationCompat.PRIORITY_DEFAULT
        }
        notificationManager.notify(DAILY_TODO_REMINDER_NOTIFICATION_ID, notification)
    }

    override fun ensureNotificationChannelsExist() {
        val channel = NotificationChannel(
            DAILY_TODO_REMINDER_CHANNEL_ID,
            context.getString(R.string.daily_todo_reminder_notification_channel),
            NotificationManager.IMPORTANCE_DEFAULT
        ).apply {
            description = context.getString(R.string.daily_todo_reminder_notification_description)
        }
        NotificationManagerCompat.from(context).createNotificationChannel(channel)
    }

    private fun dailyTodoPendingIntent(): PendingIntent? =
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

private const val DAILY_TODO_REMINDER_CHANNEL_ID = "DAILY_TODO_REMINDER"
private const val DAILY_TODO_REMINDER_NOTIFICATION_ID = 12
private const val CLICK_REQUEST_CODE = 10
private const val TARGET_ACTIVITY_NAME = "com.zn.apps.focused.MainActivity"