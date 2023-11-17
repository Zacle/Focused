package com.zn.apps.sync.workers.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.zn.apps.sync.workers.utils.SyncInitializers
import dagger.hilt.android.AndroidEntryPoint

/**
 * Receive boot completed broadcast to reset alarms but on some devices it might not be fired due to
 * batteries optimization
 */
@AndroidEntryPoint
class BootUpCompletedReceiver: BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        val action = intent?.action
        if (action != null) {
            if (action == Intent.ACTION_BOOT_COMPLETED) {
                context?.let {
                     SyncInitializers.initializeAlarmWorker(it)
                }
            }
        }
    }
}