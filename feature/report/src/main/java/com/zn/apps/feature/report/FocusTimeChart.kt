package com.zn.apps.feature.report

import android.graphics.Typeface
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.res.ResourcesCompat
import com.patrykandpatrick.vico.compose.axis.axisLabelComponent
import com.patrykandpatrick.vico.compose.axis.horizontal.rememberBottomAxis
import com.patrykandpatrick.vico.compose.axis.vertical.rememberStartAxis
import com.patrykandpatrick.vico.compose.chart.Chart
import com.patrykandpatrick.vico.compose.chart.column.columnChart
import com.patrykandpatrick.vico.compose.style.ProvideChartStyle
import com.patrykandpatrick.vico.compose.style.currentChartStyle
import com.patrykandpatrick.vico.core.axis.AxisItemPlacer
import com.patrykandpatrick.vico.core.axis.AxisPosition
import com.patrykandpatrick.vico.core.axis.formatter.AxisValueFormatter
import com.patrykandpatrick.vico.core.axis.formatter.DecimalFormatAxisValueFormatter
import com.patrykandpatrick.vico.core.chart.values.AxisValuesOverrider
import com.patrykandpatrick.vico.core.component.shape.LineComponent
import com.patrykandpatrick.vico.core.entry.ChartEntryModel
import com.patrykandpatrick.vico.core.entry.ChartEntryModelProducer
import com.zn.apps.model.data.report.CalendarReportType
import com.zn.apps.ui_design.R
import java.text.SimpleDateFormat
import java.time.OffsetDateTime
import java.util.Date
import java.util.Locale
import kotlin.math.max

@Composable
fun FocusTimeChart(
    chartEntryModelProducer: ChartEntryModelProducer,
    stats: Map<Int, Int>,
    calendarReportType: CalendarReportType,
    startDayInterval: OffsetDateTime,
    endDayInterval: OffsetDateTime,
    focusHours: Int,
    focusDays: Int,
    totalCompleted: Int,
    focusHoursDescription: String,
    focusDaysDescription: String,
    totalCompletedDescription: String,
    onNextPressed: () -> Unit,
    onPreviousPressed: () -> Unit,
    onCalendarReportTypeSelected: (CalendarReportType) -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .padding(horizontal = 8.dp, vertical = 4.dp),
        shape = MaterialTheme.shapes.medium,
        tonalElevation = 16.dp,
        shadowElevation = 4.dp
    ) {
        Column {
            CalendarReportTypeChips(
                selected = calendarReportType,
                startDayInterval = startDayInterval,
                endDayInterval = endDayInterval,
                focusHours = focusHours,
                focusDays = focusDays,
                totalCompleted = totalCompleted,
                onNextPressed = onNextPressed,
                onPreviousPressed = onPreviousPressed,
                onSelected = onCalendarReportTypeSelected,
                focusHoursDescription = focusHoursDescription,
                focusDaysDescription = focusDaysDescription,
                totalCompletedDescription = totalCompletedDescription
            )
            Spacer(modifier = Modifier.height(8.dp))

            FABarChart(
                chartEntryModelProducer = chartEntryModelProducer,
                stats = stats,
                calendarReportType = calendarReportType,
                xAxisLabels = getWeeklyAxisLabels(startDayInterval, endDayInterval)
            )

            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

private fun getWeeklyAxisLabels(
    startDayInterval: OffsetDateTime,
    endDayInterval: OffsetDateTime
): List<String> {
    val xAxisLabels = mutableListOf<String>()
    var currentDay = startDayInterval
    while (currentDay <= endDayInterval) {
        xAxisLabels.add(
            SimpleDateFormat("EEE", Locale.getDefault()).format(Date.from(currentDay.toInstant()))
        )
        currentDay = currentDay.plusDays(1)
    }
    return xAxisLabels
}

@Composable
fun FABarChart(
    chartEntryModelProducer: ChartEntryModelProducer,
    stats: Map<Int, Int>,
    calendarReportType: CalendarReportType,
    xAxisLabels: List<String>
) {
    val ySteps = 5
    val yMaxRange = max(ySteps, if (stats.isEmpty()) 0 else getMaxRange(stats.values.max(), ySteps))

    val context = LocalContext.current

    val typeFace = ResourcesCompat.getFont(context, R.font.poppins_regular) ?: Typeface.MONOSPACE

    ProvideChartStyle {
        val defaultColumns = currentChartStyle.columnChart.columns
        val color = MaterialTheme.colorScheme.onSurface.toArgb()
        Chart(
            chart = columnChart(
                axisValuesOverrider = object : AxisValuesOverrider<ChartEntryModel> {
                    override fun getMaxY(model: ChartEntryModel): Float {
                        return yMaxRange.toFloat()
                    }
                },
                columns = remember(defaultColumns) {
                    defaultColumns.map {
                        LineComponent(
                            color = color,
                            thicknessDp = it.thicknessDp,
                            shape = it.shape
                        )
                    }
                }
            ),
            chartModelProducer = chartEntryModelProducer,
            startAxis = rememberStartAxis(
                itemPlacer = AxisItemPlacer.Vertical.default(maxItemCount = START_AXIS_STEPS),
                label = axisLabelComponent(
                    typeface = typeFace
                )
            ),
            bottomAxis = rememberBottomAxis(
                label = axisLabelComponent(
                    typeface = typeFace
                ),
                valueFormatter =
                    if (calendarReportType == CalendarReportType.WEEKLY) {
                        getValueFormatter(xAxisLabels)
                    } else {
                        DecimalFormatAxisValueFormatter()
                    }
            )
        )
    }
}

private fun getValueFormatter(xAxisLabels: List<String>): AxisValueFormatter<AxisPosition.Horizontal.Bottom> {
    return AxisValueFormatter { value, _ ->
        val xValue = value.toInt()
        if (xValue >= 7)
            xValue.toString()
        else
            xAxisLabels[xValue]
    }
}

fun getMaxRange(maxValue: Int, steps: Int) =
    if (maxValue % steps == 0) maxValue else (maxValue / steps) * steps + steps

private const val START_AXIS_STEPS = 6