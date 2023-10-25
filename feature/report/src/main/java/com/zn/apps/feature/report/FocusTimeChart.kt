package com.zn.apps.feature.report

import android.graphics.Typeface
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.res.ResourcesCompat
import co.yml.charts.axis.AxisData
import co.yml.charts.ui.barchart.BarChart
import co.yml.charts.ui.barchart.models.BarChartData
import co.yml.charts.ui.barchart.models.BarChartType
import co.yml.charts.ui.barchart.models.BarStyle
import com.patrykandpatrick.vico.compose.axis.axisLabelComponent
import com.patrykandpatrick.vico.compose.axis.horizontal.rememberBottomAxis
import com.patrykandpatrick.vico.compose.axis.vertical.rememberStartAxis
import com.patrykandpatrick.vico.compose.chart.Chart
import com.patrykandpatrick.vico.compose.chart.column.columnChart
import com.patrykandpatrick.vico.compose.style.ProvideChartStyle
import com.patrykandpatrick.vico.core.axis.AxisItemPlacer
import com.patrykandpatrick.vico.core.axis.AxisPosition
import com.patrykandpatrick.vico.core.axis.formatter.AxisValueFormatter
import com.patrykandpatrick.vico.core.axis.formatter.DecimalFormatAxisValueFormatter
import com.patrykandpatrick.vico.core.chart.values.AxisValuesOverrider
import com.patrykandpatrick.vico.core.entry.ChartEntryModel
import com.patrykandpatrick.vico.core.entry.ChartEntryModelProducer
import com.zn.apps.feature.report.util.BarChartDataUtil
import com.zn.apps.model.data.report.CalendarReportType
import com.zn.apps.ui_design.R
import java.time.DayOfWeek
import java.time.OffsetDateTime
import java.time.format.TextStyle
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
                calendarReportType = calendarReportType
            )

            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
fun FABarChart(
    chartEntryModelProducer: ChartEntryModelProducer,
    stats: Map<Int, Int>,
    calendarReportType: CalendarReportType
) {
    val ySteps = 5
    val yMaxRange = max(ySteps, if (stats.isEmpty()) 0 else getMaxRange(stats.values.max(), ySteps))

    val context = LocalContext.current

    val typeFace = ResourcesCompat.getFont(context, R.font.poppins_regular) ?: Typeface.MONOSPACE

    ProvideChartStyle {
        Chart(
            chart = columnChart(
                axisValuesOverrider = object : AxisValuesOverrider<ChartEntryModel> {
                    override fun getMaxY(model: ChartEntryModel): Float {
                        return yMaxRange.toFloat()
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
                        getValueFormatter()
                    } else {
                        DecimalFormatAxisValueFormatter()
                    }
            )
        )
    }
}

private fun getValueFormatter(): AxisValueFormatter<AxisPosition.Horizontal.Bottom> {
    return AxisValueFormatter { value, _ ->
        val xValue = value.toInt()
        if (xValue < 1 || xValue > 7)
            xValue.toString()
        else
            DayOfWeek.of(xValue).getDisplayName(TextStyle.SHORT, Locale.getDefault())
    }
}

@Composable
fun FAChart(
    stats: Map<Int, Int>,
    color: Color,
    calendarReportType: CalendarReportType
) {
    val ySteps = 5
    val barData = BarChartDataUtil.getChartDataFactory(
        calendarReportType = calendarReportType,
        stats = stats,
        color = color
    )
    val yMaxRange = max(ySteps, if (stats.isEmpty()) 0 else getMaxRange(stats.keys.max(), ySteps))

    val typeFace = ResourcesCompat.getFont(LocalContext.current, R.font.poppins_regular) ?: Typeface.MONOSPACE

    val xAxisData = AxisData.Builder()
        .axisStepSize(30.dp)
        .steps(barData.size - 1)
        .bottomPadding(40.dp)
        .axisLabelAngle(20f)
        .startDrawPadding(48.dp)
        .typeFace(typeFace)
        .labelData { index -> barData[index].label }
        .build()
    val yAxisData = AxisData.Builder()
        .steps(ySteps)
        .typeFace(typeFace)
        .labelAndAxisLinePadding(20.dp)
        .axisOffset(20.dp)
        .labelData { index -> (index * (yMaxRange / ySteps)).toString() }
        .build()

    val barChartData = BarChartData(
        chartData = barData,
        xAxisData = xAxisData,
        yAxisData = yAxisData,
        barStyle = BarStyle(
            paddingBetweenBars = 20.dp,
            barWidth = 12.dp
        ),
        showYAxis = true,
        showXAxis = true,
        horizontalExtraSpace = 10.dp,
        backgroundColor = MaterialTheme.colorScheme.surface,
        barChartType = BarChartType.VERTICAL
    )
    BarChart(modifier = Modifier.height(250.dp), barChartData = barChartData)
}

fun getMaxRange(maxValue: Int, steps: Int) =
    if (maxValue % steps == 0) maxValue else (maxValue / steps) * steps + steps

private const val START_AXIS_STEPS = 6