package com.zn.apps.core.notification

import android.app.Notification
import android.content.Context
import androidx.core.app.NotificationCompat

interface Notifier {
    fun Context.createBaseNotification(
        channelId: String,
        block: NotificationCompat.Builder.() -> Unit
    ): Notification

    fun Context.ensureNotificationChannelsExist()

}