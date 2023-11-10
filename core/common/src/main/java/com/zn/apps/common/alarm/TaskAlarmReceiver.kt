package com.zn.apps.common.alarm

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.zn.apps.common.network.Dispatcher
import com.zn.apps.common.network.FocusedDispatchers.IO
import com.zn.apps.core.notification.TaskAlarmNotifier
import com.zn.apps.domain.alarm.DeleteAlarmUseCase
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class TaskAlarmReceiver: BroadcastReceiver() {

    @Inject
    @Dispatcher(IO)
    lateinit var dispatcher: CoroutineDispatcher

    @Inject
    lateinit var taskAlarmNotifier: TaskAlarmNotifier

    @Inject
    lateinit var deleteAlarmUseCase: DeleteAlarmUseCase

    @OptIn(DelicateCoroutinesApi::class)
    override fun onReceive(context: Context?, intent: Intent?) {
        Timber.d("Received intent $intent")
        if (intent != null) {
            val taskId = intent.getStringExtra(AlarmConstants.TASK_ID)
            val taskName = intent.getStringExtra(AlarmConstants.TASK_NAME)

            taskName?.let { taskAlarmNotifier.showTaskReminder(it) }

            val pendingResult = goAsync()

            GlobalScope.launch(dispatcher) {
                try {
                    if (taskId != null) {
                        deleteAlarmUseCase.execute(
                            DeleteAlarmUseCase.Request(taskId)
                        )
                    }
                } finally {
                    pendingResult.finish()
                }
            }
        }
    }
}