package com.zn.apps.domain.repository

import com.zn.apps.model.datastore.ReminderPreferences
import kotlinx.coroutines.flow.Flow

interface ReminderPreferencesRepository {

    /**
     * Stream of [ReminderPreferences]
     */
    val reminderPreferences: Flow<ReminderPreferences>

    /**
     * Set when we should send a notification to remind to do the task
     */
    suspend fun setTaskReminder(taskReminder: Int)

    /**
     * Set if we should send a daily notification to schedule a tak
     */
    suspend fun setTodoReminder(todoReminder: Boolean)

    /**
     * Set if we can snooze the reminder on the notification bar
     */
    suspend fun setSnoozeTaskReminder(snoozeTaskReminder: Boolean)

    /**
     * If we can snooze the reminder, set when we should send another reminder
     */
    suspend fun setSnoozeAfter(snoozeAfter: Int)
}