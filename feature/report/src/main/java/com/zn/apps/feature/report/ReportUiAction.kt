package com.zn.apps.feature.report

import com.zn.apps.model.data.report.CalendarReportType
import com.zn.apps.model.data.report.ReportType
import com.zn.apps.ui_common.state.UiAction

sealed class ReportUiAction: UiAction {
    data class SetCalendarReportType(val calendarReportType: CalendarReportType): ReportUiAction()
    data class SetReportType(val reportType: ReportType): ReportUiAction()
    data object NextPressed: ReportUiAction()
    data object PreviousPressed: ReportUiAction()
}
