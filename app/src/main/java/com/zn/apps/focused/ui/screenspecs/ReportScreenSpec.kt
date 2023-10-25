package com.zn.apps.focused.ui.screenspecs

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.material3.DrawerValue
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavBackStackEntry
import com.zn.apps.feature.report.R
import com.zn.apps.feature.report.ReportRoute
import com.zn.apps.feature.report.ReportUiAction
import com.zn.apps.feature.report.ReportViewModel
import com.zn.apps.focused.ui.FocusedAppState
import com.zn.apps.ui_design.icon.FAIcons
import com.zn.apps.ui_design.icon.Icon
import com.zn.apps.ui_design.icon.Icon.DrawableResourceIcon
import kotlinx.coroutines.launch

data object ReportScreenSpec: BottomNavScreenSpec {

    override val icon: Icon = DrawableResourceIcon(FAIcons.ReportDestination)

    override val label: Int = R.string.report

    override val route: String = "report"

    @Composable
    override fun Content(appState: FocusedAppState, navBackStackEntry: NavBackStackEntry) {
        val viewModel: ReportViewModel = hiltViewModel()

        val uiStateHolder by viewModel.uiStateHolder.collectAsStateWithLifecycle()
        val pomodoroReportData by viewModel.pomodoroReportData.collectAsStateWithLifecycle()
        val taskReportData by viewModel.taskReportData.collectAsStateWithLifecycle()
        val pomodoroChartEntryModelProducer = viewModel.pomodoroChartEntryModelProducer
        val taskChartEntryModelProducer = viewModel.taskChartEntryModelProducer

        ReportRoute(
            uiStateHolder = uiStateHolder,
            pomodoroReportData = pomodoroReportData,
            taskReportData = taskReportData,
            pomodoroChartEntryModelProducer = pomodoroChartEntryModelProducer,
            taskChartEntryModelProducer = taskChartEntryModelProducer,
            onReportTypePressed = {
                viewModel.submitAction(ReportUiAction.SetReportType(it))
            },
            onCalendarReportPressed = {
                viewModel.submitAction(ReportUiAction.SetCalendarReportType(it))
            },
            onNextPressed = {
                viewModel.submitAction(ReportUiAction.NextPressed)
            },
            onPreviousPressed = { viewModel.submitAction(ReportUiAction.PreviousPressed) },
            onDrawerPressed = {
                appState.coroutineScope.launch {
                    appState.drawerState.animateTo(
                        targetValue = DrawerValue.Open,
                        anim = tween(600, easing = FastOutSlowInEasing)
                    )
                }
            }
        )
    }
}