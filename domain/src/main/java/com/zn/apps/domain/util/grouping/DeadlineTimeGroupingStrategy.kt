package com.zn.apps.domain.util.grouping

import android.content.Context
import com.zn.apps.common.DeadlineTime
import com.zn.apps.common.DeadlineTimeHelper
import com.zn.apps.model.R
import com.zn.apps.model.data.task.TaskResource
import java.text.SimpleDateFormat
import java.time.OffsetDateTime
import java.util.Date
import java.util.Locale

class DeadlineTimeGroupingStrategy(
    private val context: Context,
    private val taskResources: List<TaskResource>
): GroupingStrategy {
    /**
     * Group tasks based on due date. Due dates are first converted to [DeadlineTime] and then
     * tasks with the same time are put in the same group
     */
    override fun group(): Map<String, List<TaskResource>> {
        val dateGroup = mutableMapOf<String, MutableList<TaskResource>>()
        taskResources.forEach { taskResource ->
            val date = getDeadlineTimeName(context, taskResource.task.dueDate)
            dateGroup.getOrPut(date) { mutableListOf() }.add(taskResource)
        }
        return dateGroup.toSortedMap()
    }
}

fun getDeadlineTimeName(
    context: Context,
    dueDate: OffsetDateTime?,
    locale: Locale = Locale.getDefault()
): String {
    if (dueDate == null) {
        return context.getString(R.string.no_date)
    }
    val date = Date.from(dueDate.toInstant())
    val currentDateTime = OffsetDateTime.now()

    val pattern = if (currentDateTime.year == dueDate.year)
        SimpleDateFormat("EEE, d MMM", locale)
    else {
        SimpleDateFormat("EEE, d MMM yyyy", locale)
    }

    return when(DeadlineTimeHelper.getDeadlineTime(dueDate)) {
        DeadlineTime.YESTERDAY -> context.getString(R.string.yesterday)
        DeadlineTime.TODAY -> context.getString(R.string.today)
        DeadlineTime.TOMORROW -> context.getString(R.string.tomorrow)
        DeadlineTime.THIS_DAY ->
            "${context.getString(R.string.this_day)} " +
                    SimpleDateFormat("EEEE", locale).format(date)
        DeadlineTime.OTHER -> pattern.format(date)
    }
}