package com.zn.apps.model.datastore

const val DEFAULT_TASK_REMINDER_MINUTES = 5
const val DEFAULT_SNOOZE_MINUTES = 5

data class ReminderPreferences(
    val taskReminder: Int,
    val todoReminder: Boolean,
    val snoozeTaskReminder: Boolean,
    val snoozeAfter: Int
) {
    companion object {
        val initPreferences = ReminderPreferences(
            taskReminder = DEFAULT_TASK_REMINDER_MINUTES,
            todoReminder = false,
            snoozeTaskReminder = false,
            snoozeAfter = DEFAULT_SNOOZE_MINUTES
        )
    }
}