package com.zn.apps.feature.timer.service

import android.content.Context
import android.content.Intent
import androidx.core.content.ContextCompat
import com.zn.apps.core.service.ServiceManager
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TimerServiceManager @Inject constructor(
    @ApplicationContext private val context: Context
): ServiceManager {

    override fun startService() {
        val serviceIntent = Intent(context, TimerService::class.java)
        ContextCompat.startForegroundService(context, serviceIntent)
    }

    override fun stopService() {
        val serviceIntent = Intent(context, TimerService::class.java)
        context.stopService(serviceIntent)
    }
}