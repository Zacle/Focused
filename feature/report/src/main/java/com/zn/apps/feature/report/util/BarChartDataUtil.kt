package com.zn.apps.feature.report.util

import androidx.compose.ui.graphics.Color
import co.yml.charts.axis.DataCategoryOptions
import co.yml.charts.common.model.Point
import co.yml.charts.ui.barchart.models.BarData
import com.patrykandpatrick.vico.core.entry.FloatEntry
import com.patrykandpatrick.vico.core.entry.entryOf
import com.zn.apps.model.data.report.CalendarReportType
import java.time.DayOfWeek
import java.time.format.TextStyle
import java.util.Locale

object BarChartDataUtil {

    private fun getDailyBarChartData(
        stats: Map<Int, Int>,
        color: Color,
        dataCategoryOptions: DataCategoryOptions
    ): List<BarData> {
        if (stats.isEmpty()) return getEmptyPoints()

        val barDataList = mutableListOf<BarData>()
        stats.forEach { (day, pomodoro) ->
            val point = Point(
                x = day.toFloat(),
                y = pomodoro.toFloat()
            )
            barDataList.add(
                BarData(
                    point = point,
                    color = color,
                    label = "$day",
                    dataCategoryOptions = dataCategoryOptions
                )
            )
        }

        return barDataList
    }

    private fun getWeeklyBarChartData(
        stats: Map<Int, Int>,
        color: Color,
        dataCategoryOptions: DataCategoryOptions
    ): List<BarData> {
        if (stats.isEmpty()) return getEmptyPoints()

        try {
            val barDataList = mutableListOf<BarData>()

            stats.forEach { (weekDay, pomodoro) ->
                val point = Point(
                    x = weekDay.toFloat(),
                    y = pomodoro.toFloat()
                )
                val label: String = DayOfWeek
                    .of(weekDay)
                    .getDisplayName(TextStyle.SHORT, Locale.getDefault())

                barDataList.add(
                    BarData(
                        point = point,
                        color = color,
                        dataCategoryOptions = dataCategoryOptions,
                        label = label
                    )
                )
            }

            return barDataList
        } catch (_: Exception) {}
        return getEmptyPoints()
    }

    private fun getMonthlyBarChartData(
        stats: Map<Int, Int>,
        color: Color,
        dataCategoryOptions: DataCategoryOptions
    ): List<BarData> {
        if (stats.isEmpty()) return getEmptyPoints()

        val barDataList = mutableListOf<BarData>()

        stats.forEach { (dayOfMonth, pomodoro) ->
            val point = Point(
                x = dayOfMonth.toFloat(),
                y = pomodoro.toFloat()
            )
            barDataList.add(
                BarData(
                    point = point,
                    color = color,
                    label = "$dayOfMonth",
                    dataCategoryOptions = dataCategoryOptions
                )
            )
        }

        return barDataList
    }

    private fun getEmptyPoints(): List<BarData> {
        val barDataList = mutableListOf<BarData>()
        (1..5).forEach { _ ->
            barDataList.add(
                BarData(
                    point = Point(0f, 0f)
                )
            )
        }
        return barDataList
    }

    fun getChartDataFactory(
        calendarReportType: CalendarReportType,
        stats: Map<Int, Int>,
        color: Color,
        dataCategoryOptions: DataCategoryOptions = DataCategoryOptions()
    ): List<BarData> =
        when(calendarReportType) {
            CalendarReportType.DAILY -> getDailyBarChartData(
                stats = stats,
                color = color,
                dataCategoryOptions = dataCategoryOptions
            )
            CalendarReportType.WEEKLY -> getWeeklyBarChartData(
                stats = stats,
                color = color,
                dataCategoryOptions = dataCategoryOptions
            )
            CalendarReportType.MONTHLY -> getMonthlyBarChartData(
                stats = stats,
                color = color,
                dataCategoryOptions = dataCategoryOptions
            )
        }

    private fun getDailyBarChartEntries(stats: Map<Int, Int>): List<FloatEntry> {
        if (stats.isEmpty()) return getEmptyBarChartEntries()
        val chartEntries = mutableListOf<FloatEntry>()
        stats.forEach { (day, pomodoro) ->
            chartEntries += entryOf(day, pomodoro)
        }
        return chartEntries
    }

    private fun getWeeklyBarChartEntries(stats: Map<Int, Int>): List<FloatEntry> {
        if (stats.isEmpty()) return getEmptyBarChartEntries()

        try {
            val chartEntries = mutableListOf<FloatEntry>()
            stats.forEach { (weekDay, pomodoro) ->
                chartEntries += entryOf(weekDay, pomodoro)
            }
            return chartEntries
        } catch (_: Exception) {}
        return getEmptyBarChartEntries()
    }

    private fun getMonthlyBarChartEntries(stats: Map<Int, Int>): List<FloatEntry> {
        if (stats.isEmpty()) return getEmptyBarChartEntries()
        val chartEntries = mutableListOf<FloatEntry>()
        stats.forEach { (dayOfMonth, pomodoro) ->
            chartEntries += entryOf(dayOfMonth, pomodoro)
        }
        return chartEntries
    }

    private fun getEmptyBarChartEntries(): List<FloatEntry> {
        val chartEntries = mutableListOf<FloatEntry>()
        (1..5).forEach { index ->
            chartEntries += entryOf(index, 0)
        }
        return chartEntries
    }

    fun getChartEntriesFactory(
        calendarReportType: CalendarReportType,
        stats: Map<Int, Int>
    ): List<FloatEntry> =
        when(calendarReportType) {
            CalendarReportType.DAILY -> getDailyBarChartEntries(stats)
            CalendarReportType.WEEKLY -> getWeeklyBarChartEntries(stats)
            CalendarReportType.MONTHLY -> getMonthlyBarChartEntries(stats)
        }
}