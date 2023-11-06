package com.zn.apps.data_repository.repository

import com.zn.apps.datastore.ReminderPreferencesDataSource
import com.zn.apps.domain.repository.ReminderPreferencesRepository
import com.zn.apps.model.datastore.ReminderPreferences
import kotlinx.coroutines.flow.Flow

class DefaultReminderPreferencesRepository(
    private val reminderPreferencesDataSource: ReminderPreferencesDataSource
): ReminderPreferencesRepository {

    override val reminderPreferences: Flow<ReminderPreferences>
        get() = reminderPreferencesDataSource.reminderPreferences

    override suspend fun setTaskReminder(taskReminder: Int) {
        reminderPreferencesDataSource.setTaskReminder(taskReminder)
    }

    override suspend fun setTodoReminder(todoReminder: Boolean) {
        reminderPreferencesDataSource.setTodoReminder(todoReminder)
    }

    override suspend fun setSnoozeTaskReminder(snoozeTaskReminder: Boolean) {
        reminderPreferencesDataSource.setSnoozeTaskReminder(snoozeTaskReminder)
    }

    override suspend fun setSnoozeAfter(snoozeAfter: Int) {
        reminderPreferencesDataSource.setSnoozeAfter(snoozeAfter)
    }
}