package com.zn.apps.ui_design.component

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.zn.apps.ui_design.icon.FAIcons
import java.time.OffsetDateTime

@Composable
fun DisplayTime(
    date: OffsetDateTime
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            painter = painterResource(id = FAIcons.calendar),
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurface.copy(0.5f),
            modifier = Modifier.size(12.dp)
        )
        Spacer(modifier = Modifier.size(4.dp))
        Text(
            text = formatDateForUi(dateTime = date),
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.tertiary.copy(0.5f)
        )
    }
}