package com.zn.apps.ui_design.component.dialog

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
        modifier = Modifier.padding(end = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.End
    ) {
        Row {
            Spacer(modifier = Modifier.weight(1.0f))
            Row {
                TextButton(
                    onClick = {
                        onDismissRequest(false)
                    }
                ) {
                    Text(
                        text = stringResource(id = R.string.cancel),
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.error
                    )
                }
                Button(
                    onClick = {
                        onSave(value)
                        onDismissRequest(false)
                    }
                ) {
                    Text(
                        text = stringResource(id = R.string.done),
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                }
            }
        }
    }
}