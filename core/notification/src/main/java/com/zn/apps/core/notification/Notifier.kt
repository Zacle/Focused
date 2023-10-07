package com.zn.apps.core.notification

import android.app.Notification
import androidx.core.app.NotificationCompat

interface Notifier {
    fun createBaseNotification(
        channelId: String,
        block: NotificationCompat.Builder.() -> Unit
    ): Notification

    fun ensureNotificationChannelsExist()

}