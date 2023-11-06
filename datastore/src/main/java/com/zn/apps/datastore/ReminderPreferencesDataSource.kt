package com.zn.apps.datastore

import androidx.datastore.core.DataStore
import com.zn.apps.model.datastore.ReminderPreferences
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ReminderPreferencesDataSource @Inject constructor(
    private val userPreferences: DataStore<UserPreferences>
) {
    val reminderPreferences = userPreferences.data
        .map { userPreferences ->
            ReminderPreferences(
                taskReminder = userPreferences.taskReminder,
                todoReminder = userPreferences.todoReminder,
                snoozeTaskReminder = userPreferences.snoozeTaskReminder,
                snoozeAfter = userPreferences.snoozeAfter
            )
        }

    suspend fun setTaskReminder(taskReminder: Int) {
        userPreferences.updateData {
            it.copy {
                this.taskReminder = taskReminder
            }
        }
    }

    suspend fun setTodoReminder(todoReminder: Boolean) {
        userPreferences.updateData {
            it.copy {
                this.todoReminder = todoReminder
            }
        }
    }

    suspend fun setSnoozeTaskReminder(snoozeTaskReminder: Boolean) {
        userPreferences.updateData {
            it.copy {
                this.snoozeTaskReminder = snoozeTaskReminder
            }
        }
    }

    suspend fun setSnoozeAfter(snoozeAfter: Int) {
        userPreferences.updateData {
            it.copy {
                this.snoozeAfter = snoozeAfter
            }
        }
    }
}