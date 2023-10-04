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
import android.os.Build.VERSION
import android.os.Build.VERSION_CODES
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.zn.apps.common.formatMillisecondsToString
import com.zn.apps.core.notification.TimerConstants.CLICK_REQUEST_CODE
import com.zn.apps.core.notification.TimerConstants.TARGET_ACTIVITY_NAME
import com.zn.apps.core.notification.TimerConstants.TIMER_COMPLETED_NOTIFICATION_CHANNEL_ID
import com.zn.apps.core.notification.TimerConstants.TIMER_COMPLETED_NOTIFICATION_ID
import com.zn.apps.core.notification.TimerConstants.TIMER_NOTIFICATION_CHANNEL_ID
import com.zn.apps.core.notification.TimerConstants.TIMER_NOTIFICATION_ID
import com.zn.apps.model.data.pomodoro.PomodoroPhase
import com.zn.apps.ui_design.icon.FAIcons
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TimerNotifier @Inject constructor(
    @ApplicationContext private val context: Context
): Notifier {

    private val notificationManager = NotificationManagerCompat.from(context)

    override fun Context.createBaseNotification(
        channelId: String,
        block: NotificationCompat.Builder.() -> Unit
    ): Notification {
        ensureNotificationChannelsExist()
        return NotificationCompat.Builder(
            this,
            channelId
        )
            .setSmallIcon(FAIcons.TimerDestination)
            .setSilent(true)
            .setOnlyAlertOnce(true)
            .setContentIntent(taskPendingIntent())
            .apply(block)
            .build()
    }

    fun updateTimerServiceNotification(
        currentPhase: PomodoroPhase,
        timeLeftInMillis: Long
    ) = with(context) {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        val notification = createBaseNotification(TIMER_NOTIFICATION_CHANNEL_ID) {
            setContentTitle(getString(currentPhase.phaseName))
            setContentText(formatMillisecondsToString(timeLeftInMillis))
            priority = NotificationCompat.PRIORITY_DEFAULT
        }
        notificationManager.notify(TIMER_NOTIFICATION_ID, notification)
    }

    fun showTimerCompletedNotification(completedPhase: PomodoroPhase)  = with(context){
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }

        val title: Int
        val text: Int

        when (completedPhase) {
            PomodoroPhase.POMODORO -> {
                title = R.string.pomodoro_completed
                text = R.string.pomodoro_completed_message
            }
            PomodoroPhase.SHORT_BREAK, PomodoroPhase.LONG_BREAK -> {
                title = R.string.break_over_title
                text = R.string.break_over_description
            }
        }
        val timerCompletedNotification =
            createBaseNotification(TIMER_COMPLETED_NOTIFICATION_CHANNEL_ID) {
                setContentTitle(getString(title))
                setContentText(getString(text))
                priority = NotificationCompat.PRIORITY_HIGH
                setAutoCancel(true)
            }

        notificationManager.notify(TIMER_COMPLETED_NOTIFICATION_ID, timerCompletedNotification)
    }

    override fun Context.ensureNotificationChannelsExist() {
        if (VERSION.SDK_INT < VERSION_CODES.O) return

        val timerChannel = NotificationChannel(
            TIMER_NOTIFICATION_CHANNEL_ID,
            getString(R.string.timer_notification_channel_name),
            NotificationManager.IMPORTANCE_DEFAULT
        ).apply {
            description = getString(R.string.timer_notification_channel_description)
        }

        val timerCompletedChannel = NotificationChannel(
            TIMER_COMPLETED_NOTIFICATION_CHANNEL_ID,
            getString(R.string.timer_completed_notification_channel_name),
            NotificationManager.IMPORTANCE_HIGH
        ).apply {
            description = getString(R.string.timer_completed_notification_channel_description)
        }

        // Register channels with the system
        NotificationManagerCompat.from(this).createNotificationChannels(
            listOf(timerChannel, timerCompletedChannel)
        )
    }

    fun removeTimerServiceNotification() {
        notificationManager.cancel(TIMER_NOTIFICATION_ID)
    }

    fun removeTimerCompletedNotification() {
        notificationManager.cancel(TIMER_COMPLETED_NOTIFICATION_ID)
    }

    private fun Context.taskPendingIntent(): PendingIntent? =
        PendingIntent.getActivity(
            this,
            CLICK_REQUEST_CODE,
            Intent().apply {
                action = Intent.ACTION_VIEW
                component = ComponentName(
                    packageName,
                    TARGET_ACTIVITY_NAME
                )
            },
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
}

object TimerConstants {

    const val TIMER_NOTIFICATION_CHANNEL_ID = "TIMER_NOTIFICATION_CHANNEL_ID"
    const val TIMER_COMPLETED_NOTIFICATION_CHANNEL_ID = "TIMER_COMPLETED_NOTIFICATION_CHANNEL_ID"

    const val TIMER_NOTIFICATION_ID = 7
    const val TIMER_COMPLETED_NOTIFICATION_ID = 8

    const val CLICK_REQUEST_CODE = 10
    const val TARGET_ACTIVITY_NAME = "com.zn.apps.focused.MainActivity"
}