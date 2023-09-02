package com.zn.apps.common

import com.zn.apps.model.data.task.TaskTime
import java.time.OffsetDateTime
import java.time.OffsetDateTime.now
import java.time.temporal.TemporalField
import java.time.temporal.WeekFields
import java.util.Locale

object DeadlineTimeHelper {
    /**
     * Given the seconds spent on [Task], returns the equivalence of that time
     * in hours and minutes
     *
     * @param milliSeconds seconds spent on a task
     *
     * @return [TaskTime]
     */
    fun getTaskTime(milliSeconds: Long): TaskTime {
        val hours = milliSeconds.millisecondsToHours()
        val minutes = milliSeconds.millisecondsToMinutes()

        return TaskTime(hours, minutes)
    }

    /**
     * Given a task due date, determine if the date to accomplish it is: TODAY, TOMORROW,
     * UPCOMING or SOMEDAY. A past due date is set to be accomplished TODAY
     * @param date the given due date
     *
     * @return [DeadlineType.TODAY] the task due date is today or somewhere in the past,
     *  [DeadlineType.TOMORROW] the task due date is tomorrow,
     *  [DeadlineType.UPCOMING] the task due date is somewhere in the future from after tomorrow,
     *  [DeadlineType.SOMEDAY] the task due date is not set
     */
    fun convertToDeadlineType(date: OffsetDateTime?): DeadlineType {
        if (date == null) return DeadlineType.SOMEDAY

        /* today at midnight used to get the future dates for comparisons */
        val todayAtMidnight = getTodayAtMidnight()

        /* get tomorrow at midnight to verify if the due date is tomorrow */
        val tomorrowAtMidnight = todayAtMidnight.plusDays(1)

        /* after tomorrow to determine if the due date is in the future */
        val afterTomorrowAtMidnight = tomorrowAtMidnight.plusDays(1)

        return when {
            isTomorrow(date) -> DeadlineType.TOMORROW
            date.isAfter(afterTomorrowAtMidnight) -> DeadlineType.UPCOMING
            else -> DeadlineType.TODAY
        }
    }

    /* today at midnight used to get the future dates for comparisons */
    private fun getTodayAtMidnight() = now()
        .withHour(0)
        .withMinute(0)
        .withSecond(0)

    /**
     * Check if the due date is tomorrow
     */
    private fun isTomorrow(
        dueDate: OffsetDateTime
    ): Boolean {
        val todayAtMidnight = getTodayAtMidnight()
        val tomorrowAtMidnight = todayAtMidnight.plusDays(1)
        val afterTomorrowAtMidnight = tomorrowAtMidnight.plusDays(1)

        return dueDate.isAfter(tomorrowAtMidnight) && dueDate.isBefore(afterTomorrowAtMidnight)
    }

    /**
     * Check if the due date is today
     */
    private fun isToday(
        dueDate: OffsetDateTime
    ): Boolean {
        val todayAtMidnight = getTodayAtMidnight()
        val tomorrowAtMidnight = todayAtMidnight.plusDays(1)

        return dueDate.isAfter(todayAtMidnight) && dueDate.isBefore(tomorrowAtMidnight)
    }

    /**
     * Check if the due date was yesterday
     */
    private fun isYesterday(
        dueDate: OffsetDateTime
    ): Boolean {
        val todayAtMidnight = getTodayAtMidnight()
        val yesterdayAtMidnight = todayAtMidnight.minusDays(1)

        return dueDate.isAfter(yesterdayAtMidnight) && dueDate.isBefore(todayAtMidnight)
    }

    /**
     * Check if the due date is in the same week as today
     */
    private fun isSameWeek(dueDate: OffsetDateTime, locale: Locale = Locale.getDefault()): Boolean {
        val firstDayOfWeek = firstDayOfWeek(dueDate, locale)
        if (firstDayOfWeek.monthValue != dueDate.monthValue ||
            firstDayOfWeek.year != dueDate.year)
            return false
        val daysDifference = dueDate.compareTo(firstDayOfWeek)

        return daysDifference in 0..6
    }

    /**
     * Find the first day of week to determine if the due date was scheduled in the current week
     */
    private fun firstDayOfWeek(
        dueDate: OffsetDateTime,
        locale: Locale
    ): OffsetDateTime {
        /* Get the the first day of the week in the given locale */
        val firstDayOfWeek: TemporalField = WeekFields.of(locale).dayOfWeek()

        /* After finding the first day of the week, we jump back to it and return */
        return updateTodayTimeFromDueDate(now(), dueDate).with(firstDayOfWeek, 1)
    }

    /**
     * Set Today time to due date date for comparisons to avoid time comparisons errors.
     * For example, if today is Monday, 25 August 2022 4:30PM and the due date is Tomorrow at 8:30AM,
     * The time comparison will return the same day as it is less than 24 hours
     */
    private fun updateTodayTimeFromDueDate(
        today: OffsetDateTime,
        dueDate: OffsetDateTime
    ) = today
        .withHour(dueDate.hour)
        .withMinute(dueDate.minute)
        .withSecond(dueDate.second)

    fun getDeadlineTime(date: OffsetDateTime, locale: Locale = Locale.getDefault()): DeadlineTime {
        return when {
            isToday(date) -> DeadlineTime.TODAY
            date.isBefore(now()) -> {
                if (isYesterday(date))
                    DeadlineTime.YESTERDAY
                else {
                    DeadlineTime.OTHER
                }
            }
            else -> {
                if (isTomorrow(date))
                    DeadlineTime.TOMORROW
                else if (isSameWeek(date, locale)) {
                    DeadlineTime.THIS_DAY
                } else {
                    DeadlineTime.OTHER
                }
            }
        }
    }

    /**
     * Given a [OffsetDateTime] return the name value of the enum [DeadlineTime]
     * If the [OffsetDateTime] is null, return an empty string
     */
    fun getNominalDeadlineTime(
        dateTime: OffsetDateTime?,
        locale: Locale = Locale.getDefault()
    ): String {

        if (dateTime == null) return ""

        return getDeadlineTime(dateTime, locale).name
    }
}

fun Int.minutesToMilliseconds(): Long = this * 60_000L

fun Long.millisecondsToMinutes(): Int = ((this / (1_000L * 60)) % 60).toInt()

fun Long.millisecondsToSeconds(): Int = ((this / 1_000L) % 60).toInt()

fun Long.millisecondsToHours(): Int = ((this / (1_000L * 60 * 60)) % 60).toInt()