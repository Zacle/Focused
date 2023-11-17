package com.zn.apps.sync.workers

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.ForegroundInfo
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.OutOfQuotaPolicy
import androidx.work.WorkerParameters
import com.zn.apps.common.alarm.DailyTodoReminderAlarmScheduler
import com.zn.apps.common.alarm.TaskAlarmScheduler
import com.zn.apps.common.network.Dispatcher
import com.zn.apps.common.network.FocusedDispatchers.IO
import com.zn.apps.core.notification.SyncAlarmNotifier
import com.zn.apps.domain.alarm.DeleteAlarmUseCase
import com.zn.apps.domain.alarm.GetAlarmsUseCase
import com.zn.apps.domain.datastore.GetReminderPreferencesUseCase
import com.zn.apps.model.data.alarm.AlarmItem
import com.zn.apps.sync.workers.utils.syncAlarmForegroundInfo
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import java.time.OffsetDateTime
import kotlin.Exception
import com.zn.apps.model.usecase.Result as UseCaseResult

@HiltWorker
class SyncAlarmWorker @AssistedInject constructor(
    @Assisted private val appContext: Context,
    @Assisted private val workerParams: WorkerParameters,
    @Dispatcher(IO) private val ioDispatcher: CoroutineDispatcher,
    private val getAlarmsUseCase: GetAlarmsUseCase,
    private val deleteAlarmUseCase: DeleteAlarmUseCase,
    private val getReminderPreferencesUseCase: GetReminderPreferencesUseCase,
    private val taskAlarmScheduler: TaskAlarmScheduler,
    private val dailyTodoReminderAlarmScheduler: DailyTodoReminderAlarmScheduler,
    private val syncAlarmNotifier: SyncAlarmNotifier
): CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result = withContext(ioDispatcher) {
        try {
            scheduleDailyTodoReminder()

            val response = getAlarmsUseCase.execute(GetAlarmsUseCase.Request).first()
            val alarms: List<AlarmItem>

            when(response) {
                is UseCaseResult.Success -> alarms = response.data.alarms
                else -> return@withContext Result.failure()
            }
            val maxSize = alarms.size
            alarms.forEachIndexed { index, alarmItem ->
                if (alarmItem.remindAt < OffsetDateTime.now()) {
                    deleteAlarmUseCase.execute(DeleteAlarmUseCase.Request(alarmItem.task.id))
                } else {
                    taskAlarmScheduler.scheduleAlarm(alarmItem)
                }
                setForeground(
                    syncAlarmForegroundInfo {
                        syncAlarmNotifier.showSyncAlarmNotification(maxSize, index)
                    }
                )
            }
            return@withContext Result.success()
        } catch (e: Exception) {
            return@withContext Result.failure()
        }
    }

    private suspend fun scheduleDailyTodoReminder() {
        val response = getReminderPreferencesUseCase
            .execute(GetReminderPreferencesUseCase.Request)
            .first()
        if (response is UseCaseResult.Success) {
            val shouldReschedule = response.data.reminderPreferences.todoReminder
            if (shouldReschedule) {
                dailyTodoReminderAlarmScheduler.scheduleAlarm()
            }
        }
    }

    override suspend fun getForegroundInfo(): ForegroundInfo =
        syncAlarmForegroundInfo { syncAlarmNotifier.showSyncAlarmNotification(0, 0) }

    companion object {
        fun synAlarmWork() = OneTimeWorkRequestBuilder<DelegatingWorker>()
            .setExpedited(OutOfQuotaPolicy.RUN_AS_NON_EXPEDITED_WORK_REQUEST)
            .setInputData(SyncAlarmWorker::class.delegatedData())
            .build()
    }
}