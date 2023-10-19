package com.zn.apps.feature.report

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.zn.apps.model.data.report.CalendarReportType
import com.zn.apps.model.data.report.CalendarReportType.DAILY
import com.zn.apps.model.data.report.CalendarReportType.MONTHLY
import com.zn.apps.model.data.report.CalendarReportType.WEEKLY
import com.zn.apps.ui_design.component.FocusedAppBackground
import com.zn.apps.ui_design.component.ThemePreviews
import com.zn.apps.ui_design.component.formatDateForUiInterval
import com.zn.apps.ui_design.icon.FAIcons
import com.zn.apps.ui_design.theme.FocusedAppTheme
import java.time.OffsetDateTime

@Composable
fun CalendarReportTypeChips(
    selected: CalendarReportType,
    startDayInterval: OffsetDateTime,
    endDayInterval: OffsetDateTime,
    focusHours: Int,
    focusDays: Int,
    completedPomodoro: Int,
    onNextPressed: () -> Unit,
    onPreviousPressed: () -> Unit,
    onSelected: (CalendarReportType) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            ReportChip(
                text = stringResource(id = DAILY.resId),
                selected = selected == DAILY,
                onSelected = { onSelected(DAILY) },
                modifier = Modifier
                    .weight(1f)
            )
            ReportChip(
                text = stringResource(id = WEEKLY.resId),
                selected = selected == WEEKLY,
                onSelected = { onSelected(WEEKLY) },
                modifier = Modifier
                    .weight(1f)
            )
            ReportChip(
                text = stringResource(id = MONTHLY.resId),
                selected = selected == MONTHLY,
                onSelected = { onSelected(MONTHLY) },
                modifier = Modifier
                    .weight(1f)
            )
        }
        Spacer(modifier = Modifier.height(4.dp))
        IntervalTimeRow(
            calendarReportType = selected,
            startDayInterval = startDayInterval,
            endDayInterval = endDayInterval,
            onNextPressed = onNextPressed,
            onPreviousPressed = onPreviousPressed
        )
        Spacer(modifier = Modifier.height(4.dp))
        Row {
            FocusTimeChartSummary(
                totalDone = focusHours,
                description = stringResource(id = R.string.focus_hours),
                modifier = Modifier
                    .weight(1f)
            )
            if (selected != DAILY) {
                FocusTimeChartSummary(
                    totalDone = focusDays,
                    description = stringResource(id = R.string.focus_days),
                    modifier = Modifier
                        .weight(1f)
                )
            }
            FocusTimeChartSummary(
                totalDone = completedPomodoro,
                description = stringResource(id = R.string.completed_pomodoro),
                modifier = Modifier
                    .weight(1f)
            )
        }
    }
}

@Composable
fun ReportChip(
    text: String,
    selected: Boolean,
    onSelected: () -> Unit,
    modifier: Modifier = Modifier
) {
    val backgroundColor by animateColorAsState(
        targetValue = if (selected) MaterialTheme.colorScheme.primary else Color.Transparent,
        label = "filterType backgroundColor"
    )

    val contentColor by animateColorAsState(
        targetValue = if (selected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.primary,
        label = "filterType contentColor"
    )

    Box(
        modifier = modifier
            .background(color = backgroundColor, shape = MaterialTheme.shapes.small)
            .clickable { onSelected() },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.bodySmall,
            color = contentColor,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
        )
    }
}

@Composable
fun IntervalTimeRow(
    calendarReportType: CalendarReportType,
    startDayInterval: OffsetDateTime,
    endDayInterval: OffsetDateTime,
    onNextPressed: () -> Unit,
    onPreviousPressed: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {
        IconButton(onClick = onPreviousPressed) {
            Icon(
                imageVector = FAIcons.KeyboardArrowLeft,
                contentDescription = null,
                modifier = Modifier
            )
        }
        if (calendarReportType == DAILY) {
            Text(
                text = formatDateForUiInterval(dateTime = startDayInterval),
                style = MaterialTheme.typography.bodySmall,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .weight(1f)
            )
        } else {
            Text(
                text = 
                    formatDateForUiInterval(dateTime = startDayInterval) + " - " +
                    formatDateForUiInterval(dateTime = endDayInterval),
                style = MaterialTheme.typography.bodySmall,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .weight(1f)
            )
        }
        IconButton(onClick = onNextPressed) {
            Icon(
                imageVector = FAIcons.KeyboardArrowRight,
                contentDescription = null
            )
        }
    }
}

@Composable
fun FocusTimeChartSummary(
    totalDone: Int,
    description: String,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        Text(
            text = "" + totalDone,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = description,
            style = MaterialTheme.typography.labelSmall,
            textAlign = TextAlign.Center
        )
    }
}

@ThemePreviews
@Composable
fun CalendarReportTypeChipsPreview() {
    FocusedAppTheme {
        FocusedAppBackground {
            CalendarReportTypeChips(
                selected = WEEKLY,
                startDayInterval = OffsetDateTime.now().plusDays(8),
                endDayInterval = OffsetDateTime.now().plusDays(105),
                onNextPressed = {},
                onPreviousPressed = {},
                onSelected = {},
                focusDays = 5,
                focusHours = 11,
                completedPomodoro = 8
            )
        }
    }
}