package com.zn.apps.common

import com.zn.apps.model.R


/**
 * Enum class used to group tasks that can be executed the same day
 */
enum class DeadlineType(val value: Int) {
    TODAY(R.string.today),
    TOMORROW(R.string.tomorrow),
    UPCOMING(R.string.upcoming),
    SOMEDAY(R.string.someday)
}

fun getDeadlineTypeValueFromString(dueDate: String): Int {
    DeadlineType.values().forEach {
        if (it.name.lowercase() == dueDate.lowercase()) return it.value
    }
    return 0
}