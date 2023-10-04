package com.zn.apps.feature.timer.service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import com.zn.apps.core.notification.TimerNotifier
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import javax.inject.Inject

@AndroidEntryPoint
class TimerService: Service() {

    private val scope = CoroutineScope(SupervisorJob())

    @Inject
    lateinit var timerNotifier: TimerNotifier

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        return START_STICKY
    }

    override fun onBind(p0: Intent?): IBinder? = null

    override fun onDestroy() {
        super.onDestroy()
        scope.cancel()
        timerNotifier.removeTimerServiceNotification()
    }
}