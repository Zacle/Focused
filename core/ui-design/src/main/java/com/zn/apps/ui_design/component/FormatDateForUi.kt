package com.zn.apps.ui_design.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.zn.apps.common.DeadlineTime
import com.zn.apps.common.DeadlineTimeHelper
import com.zn.apps.ui_design.R
import java.text.SimpleDateFormat
import java.time.OffsetDateTime
import java.util.Date
import java.util.Locale

@Composable
fun formatDateForUi(
    dateTime: OffsetDateTime,
    locale: Locale = Locale.getDefault()
): String {

    val date = Date.from(dateTime.toInstant())
    val currentDateTime = OffsetDateTime.now()

    val pattern = if (currentDateTime.year == dateTime.year)
        SimpleDateFormat("EEE, d MMM", locale)
    else {
        SimpleDateFormat("EEE, d MMM yyyy", locale)
    }

    return when(DeadlineTimeHelper.getDeadlineTime(dateTime)) {
        DeadlineTime.YESTERDAY -> stringResource(id = R.string.yesterday)
        DeadlineTime.TODAY -> stringResource(id = R.string.today)
        DeadlineTime.TOMORROW -> stringResource(id = R.string.tomorrow)
        DeadlineTime.THIS_DAY ->
            "${stringResource(id = R.string.this_day)} " +
                    SimpleDateFormat("EEEE", locale).format(date)
        DeadlineTime.OTHER -> pattern.format(date)
    }
}

@Composable
fun formatDateForUiInterval(
    dateTime: OffsetDateTime,
    locale: Locale = Locale.getDefault()
): String {

    val date = Date.from(dateTime.toInstant())
    val currentDateTime = OffsetDateTime.now()

    val pattern = if (currentDateTime.year == dateTime.year)
        SimpleDateFormat("MMM d", locale)
    else {
        SimpleDateFormat("MMM d, yyyy", locale)
    }

    return when(DeadlineTimeHelper.getDeadlineTime(dateTime)) {
        DeadlineTime.YESTERDAY -> stringResource(id = R.string.yesterday)
        DeadlineTime.TODAY -> stringResource(id = R.string.today)
        DeadlineTime.TOMORROW -> stringResource(id = R.string.tomorrow)
        DeadlineTime.THIS_DAY ->
            "${stringResource(id = R.string.this_day)} " +
                    SimpleDateFormat("EEEE", locale).format(date)
        DeadlineTime.OTHER -> pattern.format(date)
    }
}