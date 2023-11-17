package com.zn.apps.core.notification

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.zn.apps.ui_design.icon.FAIcons
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SyncAlarmNotifier @Inject constructor(
    @ApplicationContext private val context: Context
): Notifier {
    override fun createBaseNotification(
        channelId: String,
        block: NotificationCompat.Builder.() -> Unit
    ): Notification {
        ensureNotificationChannelsExist()
        return NotificationCompat.Builder(context, channelId)
            .setSmallIcon(FAIcons.notification)
            .setSilent(true)
            .apply(block)
            .build()
    }

    fun showSyncAlarmNotification(max: Int, progress: Int): Notification = with(context) {
        return createBaseNotification(SYNC_ALARM_CHANNEL_ID) {
            setContentTitle(getString(R.string.sync_alarm))
            if (max > 0) setProgress(max, progress, false)
            priority = NotificationCompat.PRIORITY_LOW
        }
    }

    override fun ensureNotificationChannelsExist() {
        val channel = NotificationChannel(
            SYNC_ALARM_CHANNEL_ID,
            context.getString(R.string.sync_alarm),
            NotificationManager.IMPORTANCE_LOW
        ).apply {
            description = context.getString(R.string.sync_alarm_description)
        }
        NotificationManagerCompat.from(context).createNotificationChannel(channel)
    }
}

private const val SYNC_ALARM_CHANNEL_ID = "SYNC_ALARM_CHANNEL_ID"