package com.zn.apps.feature.report

import androidx.compose.animation.animateColor
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.selection.toggleable
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.patrykandpatrick.vico.core.entry.ChartEntryModelProducer
import com.zn.apps.model.data.report.CalendarReportType
import com.zn.apps.model.data.report.ReportType
import com.zn.apps.ui_design.component.FATopAppBar
import com.zn.apps.ui_design.icon.FAIcons
import com.zn.apps.ui_design.icon.Icon

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReportRoute(
    uiStateHolder: UiStateHolder,
    pomodoroReportData: PomodoroReportData,
    taskReportData: TaskReportData,
    pomodoroChartEntryModelProducer: ChartEntryModelProducer,
    taskChartEntryModelProducer: ChartEntryModelProducer,
    onReportTypePressed: (ReportType) -> Unit,
    onCalendarReportPressed: (CalendarReportType) -> Unit,
    onNextPressed: () -> Unit,
    onPreviousPressed: () -> Unit,
    onDrawerPressed: () -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        containerColor = Color.Transparent,
        contentColor = MaterialTheme.colorScheme.onBackground,
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        topBar = {
            FATopAppBar(
                titleName = stringResource(id = R.string.report),
                navigationIcon = Icon.DrawableResourceIcon(FAIcons.menu),
                onNavigationIconClicked = onDrawerPressed
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    ReportFilterChip(
                        reportType = ReportType.POMODORO,
                        selected = uiStateHolder.reportType == ReportType.POMODORO,
                        onReportTypeSelected = onReportTypePressed
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    ReportFilterChip(
                        reportType = ReportType.TASK,
                        selected = uiStateHolder.reportType == ReportType.TASK,
                        onReportTypeSelected = onReportTypePressed
                    )
                }
            }
        },
        modifier = modifier
    ) { innerPadding ->
        ReportScreen(
            uiStateHolder = uiStateHolder,
            pomodoroReportData = pomodoroReportData,
            taskReportData = taskReportData,
            pomodoroChartEntryModelProducer = pomodoroChartEntryModelProducer,
            taskChartEntryModelProducer = taskChartEntryModelProducer,
            onCalendarReportPressed = onCalendarReportPressed,
            onNextPressed = onNextPressed,
            onPreviousPressed = onPreviousPressed,
            contentPadding = innerPadding
        )
    }
}

@Composable
fun ReportScreen(
    uiStateHolder: UiStateHolder,
    pomodoroReportData: PomodoroReportData,
    taskReportData: TaskReportData,
    pomodoroChartEntryModelProducer: ChartEntryModelProducer,
    taskChartEntryModelProducer: ChartEntryModelProducer,
    onCalendarReportPressed: (CalendarReportType) -> Unit,
    onNextPressed: () -> Unit,
    onPreviousPressed: () -> Unit,
    modifier: Modifier = Modifier,
    contentPadding : PaddingValues = PaddingValues()
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = contentPadding
    ) {
        item {
            if (uiStateHolder.reportType == ReportType.POMODORO) {
                ReportOverview(
                    focusHours = pomodoroReportData.totalHours,
                    totalCompleted = pomodoroReportData.totalCompleted,
                    hoursDescription = stringResource(id = R.string.total_focus_hours),
                    completedDescription = stringResource(id = R.string.total_completed_pomodoro)
                )
            } else {
                ReportOverview(
                    focusHours = taskReportData.totalHours,
                    totalCompleted = taskReportData.totalCompleted,
                    hoursDescription = stringResource(id = R.string.total_focus_hours),
                    completedDescription = stringResource(id = R.string.total_completed_tasks)
                )
            }
        }
        item {
            if (uiStateHolder.reportType == ReportType.POMODORO) {
                FocusTimeChart(
                    calendarReportType = uiStateHolder.pomodoroTimeStateHolder.calendarReportType,
                    startDayInterval = uiStateHolder.reportInterval.startTime,
                    endDayInterval = uiStateHolder.reportInterval.endTime,
                    chartEntryModelProducer = pomodoroChartEntryModelProducer,
                    stats = pomodoroReportData.statsReport.stats,
                    focusHours = pomodoroReportData.statsReport.focusHours,
                    focusDays = pomodoroReportData.statsReport.focusDays,
                    totalCompleted = pomodoroReportData.statsReport.totalCompleted,
                    focusHoursDescription = stringResource(id = R.string.focus_hours),
                    focusDaysDescription = stringResource(id = R.string.focus_days),
                    totalCompletedDescription = stringResource(id = R.string.completed_pomodoro),
                    onNextPressed = onNextPressed,
                    onPreviousPressed = onPreviousPressed,
                    onCalendarReportTypeSelected = onCalendarReportPressed
                )
            } else {
                FocusTimeChart(
                    chartEntryModelProducer = taskChartEntryModelProducer,
                    calendarReportType = uiStateHolder.taskTimeStateHolder.calendarReportType,
                    startDayInterval = uiStateHolder.reportInterval.startTime,
                    endDayInterval = uiStateHolder.reportInterval.endTime,
                    focusHours = taskReportData.statsReport.focusHours,
                    focusDays = taskReportData.statsReport.focusDays,
                    totalCompleted = taskReportData.statsReport.totalCompleted,
                    stats = taskReportData.statsReport.stats,
                    focusHoursDescription = stringResource(id = R.string.focus_hours),
                    focusDaysDescription = stringResource(id = R.string.focus_days),
                    totalCompletedDescription = stringResource(id = R.string.completed_tasks),
                    onNextPressed = onNextPressed,
                    onPreviousPressed = onPreviousPressed,
                    onCalendarReportTypeSelected = onCalendarReportPressed
                )
            }
        }
    }
}

@Composable
fun ReportFilterChip(
    reportType: ReportType,
    selected: Boolean,
    onReportTypeSelected: (ReportType) -> Unit,
    modifier: Modifier = Modifier
) {
    val transition = updateTransition(targetState = selected, label = "tag transition")

    val backgroundColor by transition.animateColor(
        label = "report filter backgroundColor",
        transitionSpec = { spring(dampingRatio = Spring.DampingRatioMediumBouncy) }
    ) { isSelected ->
        if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondaryContainer
    }

    val contentColor by transition.animateColor(
        label = "report filter contentColor",
        transitionSpec = { tween(durationMillis = 300, easing = LinearOutSlowInEasing) }
    ) { isSelected ->
        if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSecondaryContainer
    }

    val borderColor by transition.animateColor(
        label = "report filter borderColor",
        transitionSpec = { tween(durationMillis = 300, easing = FastOutLinearInEasing) }
    ) { isSelected ->
        if (!isSelected) MaterialTheme.colorScheme.primary else Color.Transparent
    }

    Surface(
        color = backgroundColor,
        contentColor = contentColor,
        shape = MaterialTheme.shapes.extraLarge,
        modifier = modifier
            .border(
                width = 2.dp,
                color = borderColor,
                shape = MaterialTheme.shapes.extraLarge
            )
    ) {

        val interactionSource = remember { MutableInteractionSource() }

        val pressed by interactionSource.collectIsPressedAsState()
        val backgroundPressed =
            if (pressed)
                Modifier.background(MaterialTheme.colorScheme.primary)
            else
                Modifier.background(Color.Transparent)

        Box(modifier = Modifier
            .toggleable(
                value = selected,
                onValueChange = { onReportTypeSelected(reportType) },
                interactionSource = interactionSource,
                indication = null
            )
            .then(backgroundPressed)
        ) {
            Text(
                text = stringResource(id = reportType.resId),
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(horizontal = 24.dp, vertical = 8.dp),
                fontWeight = FontWeight.Bold
            )
        }
    }
}