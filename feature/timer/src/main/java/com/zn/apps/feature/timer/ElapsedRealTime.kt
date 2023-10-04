package com.zn.apps.feature.timer

import android.os.SystemClock
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ElapsedRealTime @Inject constructor(): ElapsedTime {
    override val elapsedTime: Long
        get() = SystemClock.elapsedRealtime()
}