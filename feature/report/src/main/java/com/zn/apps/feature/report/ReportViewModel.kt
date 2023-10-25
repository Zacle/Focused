package com.zn.apps.feature.report

import androidx.lifecycle.viewModelScope
import com.patrykandpatrick.vico.core.entry.ChartEntryModelProducer
import com.zn.apps.domain.report.GetPomodoroStatsUseCase
import com.zn.apps.domain.task.GetTaskStatsUseCase
import com.zn.apps.domain.util.report.CalendarReport
import com.zn.apps.feature.report.ReportUiAction.NextPressed
import com.zn.apps.feature.report.ReportUiAction.PreviousPressed
import com.zn.apps.feature.report.ReportUiAction.SetCalendarReportType
import com.zn.apps.feature.report.ReportUiAction.SetReportType
import com.zn.apps.feature.report.util.BarChartDataUtil
import com.zn.apps.model.data.report.CalendarReportType
import com.zn.apps.model.data.report.ReportInterval
import com.zn.apps.model.data.report.ReportType
import com.zn.apps.model.data.report.StatsReport
import com.zn.apps.model.usecase.Result
import com.zn.apps.ui_common.state.BaseViewModel
import com.zn.apps.ui_common.state.UiEvent
import com.zn.apps.ui_common.state.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import java.time.OffsetDateTime
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class ReportViewModel @Inject constructor(
    private val pomodoroStatsUseCase: GetPomodoroStatsUseCase,
    private val taskStatsUseCase: GetTaskStatsUseCase
): BaseViewModel<Unit, UiState<Unit>, ReportUiAction, UiEvent>() {

    private var pomodoroTimeStateHolder = MutableStateFlow(PomodoroTimeStateHolder())
    private var taskTimeStateHolder = MutableStateFlow(TaskTimeStateHolder())

    val pomodoroChartEntryModelProducer: ChartEntryModelProducer = ChartEntryModelProducer()
    val taskChartEntryModelProducer: ChartEntryModelProducer = ChartEntryModelProducer()

    var uiStateHolder = MutableStateFlow(UiStateHolder())
        private set

    /**
     * Report data for pomodoro that listens to every change on the calendar report type:
     * [CalendarReportType.DAILY], [CalendarReportType.WEEKLY] or [CalendarReportType.MONTHLY].
     *
     * Once the data has been fetched, we then set them the the chart entries to be displayed on
     * the chart
     */
    val pomodoroReportData: StateFlow<PomodoroReportData> = pomodoroTimeStateHolder
        .onEach { stateHolder ->
            uiStateHolder.update {
                it.copy(
                    reportInterval = getInterval(
                        calendarReportType = stateHolder.calendarReportType,
                        currentDateTime = stateHolder.currentDateTime
                    ),
                    pomodoroTimeStateHolder = stateHolder
                )
            }
        }
        .flatMapLatest { stateHolder ->
            pomodoroStatsUseCase.execute(
                GetPomodoroStatsUseCase.Request(
                    calendarReportType = stateHolder.calendarReportType,
                    dateTime = stateHolder.currentDateTime
                )
            ).mapLatest { result ->
                if (result is Result.Success) {
                    val response = result.data
                    pomodoroChartEntryModelProducer.setEntries(
                        BarChartDataUtil.getChartEntriesFactory(
                            calendarReportType = stateHolder.calendarReportType,
                            stats = response.statsReport.stats
                        )
                    )
                    PomodoroReportData(
                        totalHours = response.totalHours,
                        totalCompleted = response.totalCompleted,
                        statsReport = response.statsReport
                    )
                }
                else {
                    PomodoroReportData()
                }
            }
        }
        .stateIn(viewModelScope, SharingStarted.Eagerly, PomodoroReportData())

    /**
     * Report data for tasks that listens to every change on the calendar report type:
     * [CalendarReportType.DAILY], [CalendarReportType.WEEKLY] or [CalendarReportType.MONTHLY].
     *
     * Once the data has been fetched, we then set them the the chart entries to be displayed on
     * the chart
     */
    val taskReportData: StateFlow<TaskReportData> = taskTimeStateHolder
        .onEach { stateHolder ->
            uiStateHolder.update {
                it.copy(
                    reportInterval = getInterval(
                        calendarReportType = stateHolder.calendarReportType,
                        currentDateTime = stateHolder.currentDateTime
                    ),
                    taskTimeStateHolder = stateHolder
                )
            }
        }
        .flatMapLatest { stateHolder ->
            taskStatsUseCase.execute(
                GetTaskStatsUseCase.Request(
                    calendarReportType = stateHolder.calendarReportType,
                    dateTime = stateHolder.currentDateTime
                )
            ).mapLatest { result ->
                if (result is Result.Success) {
                    val response = result.data
                    taskChartEntryModelProducer.setEntries(
                        BarChartDataUtil.getChartEntriesFactory(
                            calendarReportType = stateHolder.calendarReportType,
                            stats = response.statsReport.stats
                        )
                    )
                    TaskReportData(
                        totalHours = response.totalHours,
                        totalCompleted = response.totalCompleted,
                        statsReport = response.statsReport
                    )
                }
                else {
                    TaskReportData()
                }
            }
        }
        .stateIn(viewModelScope, SharingStarted.Eagerly, TaskReportData())

    override fun initState(): UiState<Unit> = UiState.Loading

    override fun handleAction(action: ReportUiAction) {
        when(action) {
            is SetCalendarReportType -> setCalendarReportType(action.calendarReportType)
            is SetReportType -> setReportType(action.reportType)
            NextPressed -> nextPressed()
            PreviousPressed -> previousPressed()
        }
    }

    private fun previousPressed() {
        if (uiStateHolder.value.reportType == ReportType.POMODORO) {
            when(pomodoroTimeStateHolder.value.calendarReportType) {
                CalendarReportType.DAILY -> {
                    val currentDate = pomodoroTimeStateHolder.value.currentDateTime
                    pomodoroTimeStateHolder.update {
                        it.copy(currentDateTime = currentDate.minusDays(1))
                    }
                }
                CalendarReportType.WEEKLY -> {
                    val currentDate = pomodoroTimeStateHolder.value.currentDateTime
                    pomodoroTimeStateHolder.update {
                        it.copy(currentDateTime = currentDate.minusWeeks(1))
                    }
                }
                CalendarReportType.MONTHLY -> {
                    val currentDate = pomodoroTimeStateHolder.value.currentDateTime
                    pomodoroTimeStateHolder.update {
                        it.copy(currentDateTime = currentDate.minusMonths(1))
                    }
                }
            }
        } else {
            when(taskTimeStateHolder.value.calendarReportType) {
                CalendarReportType.DAILY -> {
                    val currentDate = taskTimeStateHolder.value.currentDateTime
                    taskTimeStateHolder.update {
                        it.copy(currentDateTime = currentDate.minusDays(1))
                    }
                }
                CalendarReportType.WEEKLY -> {
                    val currentDate = taskTimeStateHolder.value.currentDateTime
                    taskTimeStateHolder.update {
                        it.copy(currentDateTime = currentDate.minusWeeks(1))
                    }
                }
                CalendarReportType.MONTHLY -> {
                    val currentDate = taskTimeStateHolder.value.currentDateTime
                    taskTimeStateHolder.update {
                        it.copy(currentDateTime = currentDate.minusMonths(1))
                    }
                }
            }
        }
    }

    private fun nextPressed() {
        if (uiStateHolder.value.reportType == ReportType.POMODORO) {
            when(pomodoroTimeStateHolder.value.calendarReportType) {
                CalendarReportType.DAILY -> {
                    val currentDate = pomodoroTimeStateHolder.value.currentDateTime
                    pomodoroTimeStateHolder.update {
                        it.copy(currentDateTime = currentDate.plusDays(1))
                    }
                }
                CalendarReportType.WEEKLY -> {
                    val currentDate = pomodoroTimeStateHolder.value.currentDateTime
                    pomodoroTimeStateHolder.update {
                        it.copy(currentDateTime = currentDate.plusDays(7))
                    }
                }
                CalendarReportType.MONTHLY -> {
                    val currentDate = pomodoroTimeStateHolder.value.currentDateTime
                    pomodoroTimeStateHolder.update {
                        it.copy(
                            currentDateTime = currentDate
                                .plusMonths(1)
                                .withDayOfMonth(1)
                        )
                    }
                }
            }
        } else {
            when(taskTimeStateHolder.value.calendarReportType) {
                CalendarReportType.DAILY -> {
                    val currentDate = taskTimeStateHolder.value.currentDateTime
                    taskTimeStateHolder.update {
                        it.copy(currentDateTime = currentDate.plusDays(1))
                    }
                }
                CalendarReportType.WEEKLY -> {
                    val currentDate = taskTimeStateHolder.value.currentDateTime
                    taskTimeStateHolder.update {
                        it.copy(currentDateTime = currentDate.plusDays(7))
                    }
                }
                CalendarReportType.MONTHLY -> {
                    val currentDate = taskTimeStateHolder.value.currentDateTime
                    taskTimeStateHolder.update {
                        it.copy(
                            currentDateTime = currentDate
                                .plusMonths(1)
                                .withDayOfMonth(1)
                        )
                    }
                }
            }
        }
    }

    private fun getInterval(
        calendarReportType: CalendarReportType,
        currentDateTime: OffsetDateTime
    ): ReportInterval =
        CalendarReport.getCalendarReport(calendarReportType, currentDateTime).getReportInterval()

    private fun setReportType(reportType: ReportType) {
        uiStateHolder.update {
            it.copy(reportType = reportType)
        }
    }

    private fun setCalendarReportType(calendarReportType: CalendarReportType) {
        when(uiStateHolder.value.reportType) {
            ReportType.POMODORO -> {
                pomodoroTimeStateHolder.update {
                    it.copy(
                        calendarReportType = calendarReportType,
                        currentDateTime = OffsetDateTime.now()
                    )
                }
            }
            ReportType.TASK -> {
                taskTimeStateHolder.update {
                    it.copy(
                        calendarReportType = calendarReportType,
                        currentDateTime = OffsetDateTime.now()
                    )
                }
            }
        }
    }
}

data class PomodoroTimeStateHolder(
    val calendarReportType: CalendarReportType = CalendarReportType.WEEKLY,
    val currentDateTime: OffsetDateTime = OffsetDateTime.now()
)

data class TaskTimeStateHolder(
    val calendarReportType: CalendarReportType = CalendarReportType.WEEKLY,
    val currentDateTime: OffsetDateTime = OffsetDateTime.now()
)

data class UiStateHolder(
    val reportType: ReportType = ReportType.POMODORO,
    val reportInterval: ReportInterval = ReportInterval(
        startTime = OffsetDateTime.now(),
        endTime = OffsetDateTime.now()
    ),
    val pomodoroTimeStateHolder: PomodoroTimeStateHolder = PomodoroTimeStateHolder(),
    val taskTimeStateHolder: TaskTimeStateHolder = TaskTimeStateHolder()
)

data class PomodoroReportData(
    val totalHours: Int = 0,
    val totalCompleted: Int = 0,
    val statsReport: StatsReport = StatsReport()
)

data class TaskReportData(
    val totalHours: Int = 0,
    val totalCompleted: Int = 0,
    val statsReport: StatsReport = StatsReport()
)