package com.zn.apps.ui_design.component.dialog

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.zn.apps.ui_design.R

@Composable
fun <T> DialogDoneOrCancel(
    onDismissRequest: (Boolean) -> Unit,
    value: T,
    onSave: (T) -> Unit
) {
    Row(
        modifier = Modifier.padding(end = 8.dp, bottom = 2.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.End
    ) {
        Spacer(modifier = Modifier.weight(1.0f))
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextButton(
                onClick = {
                    onDismissRequest(false)
                }
            ) {
                Text(
                    text = stringResource(id = R.string.cancel),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.error
                )
            }
            Box(
                modifier = Modifier
                    .clickable {
                        onSave(value)
                        onDismissRequest(false)
                    }
                    .clip(MaterialTheme.shapes.large)
                    .background(MaterialTheme.colorScheme.primary)
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = stringResource(id = R.string.done),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onPrimary
                )
            }
        }
    }
}