package com.zn.apps.common

import com.zn.apps.model.R


enum class DeadlineTime(val value: Int) {
    YESTERDAY(R.string.yesterday),
    TODAY(R.string.today),
    TOMORROW(R.string.tomorrow),
    THIS_DAY(R.string.this_day),
    OTHER(0)
}