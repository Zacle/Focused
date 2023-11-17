package com.zn.apps.sync.workers.utils

import android.content.Context
import androidx.work.ExistingWorkPolicy
import androidx.work.WorkManager
import com.zn.apps.sync.workers.SyncAlarmWorker

object SyncInitializers {
    fun initializeAlarmWorker(context: Context) {
        WorkManager.getInstance(context).apply {
            enqueueUniqueWork(
                SYNC_ALARM_NAME,
                ExistingWorkPolicy.KEEP,
                SyncAlarmWorker.synAlarmWork()
            )
        }
    }
}

internal const val SYNC_ALARM_NAME = "AlarmSync"