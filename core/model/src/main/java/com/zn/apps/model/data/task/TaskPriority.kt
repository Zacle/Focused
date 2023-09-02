package com.zn.apps.model.data.task

import com.zn.apps.model.R


enum class TaskPriority(val value: Int) {
    HIGH(R.string.high_priority),
    MEDIUM(R.string.medium_priority),
    LOW(R.string.low_priority),
    NONE(R.string.no_priority);
}