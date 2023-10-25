package com.zn.apps.feature.report

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.zn.apps.ui_design.component.ThemePreviews
import com.zn.apps.ui_design.theme.FocusedAppTheme

@Composable
fun ReportOverview(
    focusHours: Int,
    totalCompleted: Int,
    hoursDescription: String,
    completedDescription: String,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .padding(horizontal = 16.dp, vertical = 12.dp),
        shape = MaterialTheme.shapes.medium,
        tonalElevation = 16.dp,
        shadowElevation = 4.dp
    ) {
        Column(
            modifier = Modifier
                .padding(horizontal = 12.dp, vertical = 8.dp)
        ) {
            Text(
                text = stringResource(id = R.string.overview)
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier
                    .padding(top = 8.dp)
            ) {
                OverviewData(
                    totalDone = focusHours,
                    description = hoursDescription,
                    modifier = Modifier
                        .weight(1f)
                )
                OverviewData(
                    totalDone = totalCompleted,
                    description = completedDescription,
                    modifier = Modifier
                        .weight(1f)
                )
            }
        }
    }
}

@Composable
fun OverviewData(
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
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = description,
            style = MaterialTheme.typography.bodySmall,
            textAlign = TextAlign.Center
        )
    }
}

@ThemePreviews
@Composable
fun PomodoroOverviewPreview() {
    FocusedAppTheme {
        ReportOverview(
            focusHours = 69,
            totalCompleted = 27,
            hoursDescription = stringResource(id = R.string.total_focus_hours),
            completedDescription = stringResource(id = R.string.total_completed_pomodoro)
        )
    }
}