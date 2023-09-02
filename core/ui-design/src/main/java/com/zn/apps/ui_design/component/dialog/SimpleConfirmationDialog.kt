package com.zn.apps.ui_design.component.dialog

import androidx.annotation.StringRes
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.zn.apps.ui_design.R

@Composable
fun SimpleConfirmationDialog(
    @StringRes title: Int,
    @StringRes text: Int,
    confirmationAction: () -> Unit,
    cancelAction: () -> Unit
) {
    AlertDialog(
        onDismissRequest = cancelAction,
        title = {
            Text(text = stringResource(id = title))
        },
        text = {
            Text(text = stringResource(id = text))
        },
        confirmButton = {
            TextButton(onClick = { confirmationAction() }) {
                Text(text = stringResource(id = R.string.yes))
            }
        },
        dismissButton = {
            TextButton(onClick = { cancelAction() }) {
                Text(text = stringResource(id = R.string.no))
            }
        }
    )
}