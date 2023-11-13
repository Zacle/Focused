package com.zn.apps.common.alarm

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.zn.apps.core.notification.DailyTodoReminderNotifier
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class DailyTodoReminderAlarmReceiver: BroadcastReceiver() {

    @Inject
    lateinit var dailyTodoReminderNotifier: DailyTodoReminderNotifier

    override fun onReceive(context: Context?, intent: Intent?) {
        dailyTodoReminderNotifier.showDailyTodoReminder()
    }
}