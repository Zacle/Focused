package com.zn.apps.feature.timer.service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import com.zn.apps.core.notification.TimerConstants.TIMER_NOTIFICATION_CHANNEL_ID
import com.zn.apps.core.notification.TimerConstants.TIMER_NOTIFICATION_ID
import com.zn.apps.core.notification.TimerNotifier
import com.zn.apps.feature.timer.TimerManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class TimerService: Service() {

    private val scope = CoroutineScope(SupervisorJob())

    @Inject
    lateinit var timerNotifier: TimerNotifier

    @Inject
    lateinit var timerManager: TimerManager

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        startForeground(
            TIMER_NOTIFICATION_ID,
            timerNotifier.createBaseNotification(
                TIMER_NOTIFICATION_CHANNEL_ID
            ) {}
        )

        scope.launch {
            timerManager.pomodoroTimerState.collectLatest { pomodoroState ->
                timerNotifier.updateTimerServiceNotification(
                    currentPhase = pomodoroState.currentPhase,
                    timeLeftInMillis = pomodoroState.timeLeftInMillis
                )
            }
        }

        return START_STICKY
    }

    override fun onBind(p0: Intent?): IBinder? = null

    override fun onDestroy() {
        super.onDestroy()
        scope.cancel()
        timerNotifier.removeTimerServiceNotification()
    }
}