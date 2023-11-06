package com.zn.apps.model.datastore

data class ReminderPreferences(
    val taskReminder: Int,
    val todoReminder: Boolean,
    val snoozeTaskReminder: Boolean,
    val snoozeAfter: Int
)