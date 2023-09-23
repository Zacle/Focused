package com.zn.apps.ui_common

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.zn.apps.model.data.task.RelatedTasksMetaDataResult
import com.zn.apps.ui_design.R.string
import com.zn.apps.ui_design.icon.FAIcons

@Composable
fun TaskListSection(
    sectionTitle: String,
    tasksMetaDataResult: RelatedTasksMetaDataResult,
    modifier: Modifier = Modifier,
    onRelatedTasksSelected: (() -> Unit)? = null
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(modifier = Modifier.weight(1.0f)) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = sectionTitle,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.padding(end = 8.dp)
                )
                val color = MaterialTheme.colorScheme.onSurface
                Canvas(modifier = Modifier
                    .size(4.dp)
                ) {
                    drawCircle(color = color)
                }
                var estimatedTime = ""
                if (tasksMetaDataResult.estimatedTime.hours > 0) estimatedTime += tasksMetaDataResult.estimatedTime.hours.toString() + "h"
                if (tasksMetaDataResult.estimatedTime.minutes > 0) estimatedTime += tasksMetaDataResult.estimatedTime.minutes.toString() + "m"
                Text(
                    text = estimatedTime,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.tertiary,
                    modifier = Modifier.padding(start = 8.dp)
                )
            }
        }

        if (onRelatedTasksSelected != null) {
            TextButton(onClick = { onRelatedTasksSelected() }) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = stringResource(id = string.see_all),
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.secondary
                    )
                    Icon(
                        imageVector = FAIcons.ArrowForward,
                        contentDescription = stringResource(id = string.see_all),
                        tint = MaterialTheme.colorScheme.secondary,
                        modifier = Modifier.size(12.dp)
                    )
                }
            }
        }
    }
}