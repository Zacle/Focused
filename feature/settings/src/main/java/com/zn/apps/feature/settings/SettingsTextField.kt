package com.zn.apps.feature.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.zn.apps.ui_design.R
import com.zn.apps.ui_design.component.DropDownItem
import com.zn.apps.ui_design.component.FocusedAppBackground
import com.zn.apps.ui_design.component.ThemePreviews
import com.zn.apps.ui_design.icon.FAIcons
import com.zn.apps.ui_design.theme.FocusedAppTheme

private val defaultPaddingValues = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
private val dividerPaddingValues = PaddingValues(horizontal = 16.dp, vertical = 4.dp)

@Composable
fun SettingsTextField(
    title: String,
    selection: String,
    onClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center
    ) {
        Row(
            modifier = Modifier
                .clickable { onClicked() }
                .padding(defaultPaddingValues),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.weight(1f)
            )
            Text(
                text = selection,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.tertiary.copy(alpha = 0.6f),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.width(2.dp))
            Icon(
                imageVector = FAIcons.KeyboardArrowRight,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .size(24.dp)
            )
        }
        Divider(
            modifier = Modifier
                .padding(dividerPaddingValues)
        )
    }
}

@Composable
fun SettingsTextFieldSwitch(
    title: String,
    checked: Boolean,
    onCheckedChanged: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center
    ) {
        Row(
            modifier = Modifier
                .padding(defaultPaddingValues),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.weight(1f)
            )
            Switch(
                checked = checked,
                onCheckedChange = onCheckedChanged,
                modifier = Modifier
                    .size(24.dp)
            )
        }
        Divider(
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 4.dp)
        )
    }
}

@Composable
fun SettingsTextFiledDropdown(
    title: String,
    taskReminderAfter: Int,
    onClicked: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center
    ) {
        Row(
            modifier = Modifier
                .padding(defaultPaddingValues),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.weight(1f)
            )
            Box(modifier = Modifier.clickable { expanded = true }) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "" + taskReminderAfter + " " + stringResource(id = R.string.minutes_before),
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Icon(
                        imageVector = FAIcons.KeyboardArrowDown,
                        contentDescription = null
                    )
                }
                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                    modifier = Modifier.background(MaterialTheme.colorScheme.tertiaryContainer)
                ) {
                    (1..6).forEach { index ->
                        DropDownItem(
                            item = index * 5,
                            onItemSelected = { onClicked(index * 5) },
                            onDropDownExpanded = { expanded = false },
                            text = "" + (index * 5) + " " + stringResource(id = R.string.minutes_before),
                            color = Color.Transparent
                        )
                    }
                }
            }
        }
        Divider(
            modifier = Modifier
                .padding(dividerPaddingValues)
        )
    }
}

@ThemePreviews
@Composable
fun SettingsTextFieldSwitchPreview() {
    FocusedAppTheme {
        FocusedAppBackground {
            SettingsTextFieldSwitch(
                title = "Disable break",
                checked = false,
                onCheckedChanged = {},
            )
        }
    }
}

@ThemePreviews
@Composable
fun SettingsTextFieldPreview() {
    FocusedAppTheme {
        FocusedAppBackground {
            SettingsTextField(
                title = "Sound while focusing",
                selection = "Tic-tac",
                onClicked = {}
            )
        }
    }
}

@ThemePreviews
@Composable
fun SettingsTextFiledDropdownPreview() {
    FocusedAppTheme {
        FocusedAppBackground {
            SettingsTextFiledDropdown(
                title = "Task reminder default",
                taskReminderAfter = 10,
                onClicked = {}
            )
        }
    }
}