package com.zn.apps.ui_design.component

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.zn.apps.model.data.tag.Tag
import com.zn.apps.model.data.task.TaskPriority
import com.zn.apps.ui_design.R
import com.zn.apps.ui_design.icon.FAIcons
import com.zn.apps.ui_design.icon.FormIcon
import com.zn.apps.ui_design.icon.Icon.DrawableResourceIcon
import com.zn.apps.ui_design.theme.FocusedAppTheme
import com.zn.apps.ui_design.util.getPriorityColor
import com.zn.apps.ui_design.util.uiColor

@Composable
fun FAInputSelector(
    backgroundColor: Color,
    icon: DrawableResourceIcon? = null,
    onClick: () -> Unit,
    text: String,
    label: String = "",
    withDropdownIcon: Boolean = true
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
    ) {
        Row(
            modifier = Modifier.weight(1f),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (icon != null) {
                FormIcon(
                    color = backgroundColor,
                    icon = icon,
                    modifier = Modifier.padding(end = 16.dp)
                )
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                Text(
                    text = text,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontWeight = FontWeight.Bold
                )
                if (withDropdownIcon) {
                    Icon(
                        imageVector = FAIcons.KeyboardArrowDown,
                        contentDescription = text,
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        }
        if (label.isNotEmpty()) {
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.End
            )
        }
    }
}

@Composable
fun ChoosePriority(
    onPrioritySelected: (TaskPriority) -> Unit,
    selectedPriority: TaskPriority
) {
    var expanded by remember {
        mutableStateOf(false)
    }
    Box {
        IconButton(onClick = { expanded = true }) {
            Icon(
                painter = painterResource(id = FAIcons.priority),
                contentDescription = stringResource(id = R.string.select_priority),
                tint = getPriorityColor(selectedPriority)
            )
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.background(MaterialTheme.colorScheme.background.copy(alpha = 0.8f))
        ) {
            DropDownTitle(text = stringResource(id = R.string.select_priority))
            TaskPriority.values().forEach { taskPriority ->
                DropDownItem(
                    item = taskPriority,
                    onItemSelected = { onPrioritySelected(taskPriority) },
                    onDropDownExpanded = { expanded = false },
                    text = stringResource(id = taskPriority.value),
                    color = getPriorityColor(taskPriority)
                )
            }
        }
    }
}

@Composable
fun ChooseTag(
    tags: List<Tag>,
    selectedTag: Tag?,
    onTagSelected: (Tag?) -> Unit,
) {
    var tagDropDownExpanded by remember {
        mutableStateOf(false)
    }
    Box {
        IconButton(onClick = { tagDropDownExpanded = true }) {
            Icon(
                painter = painterResource(id = FAIcons.tag),
                contentDescription = stringResource(id = R.string.select_tag),
                tint = selectedTag?.uiColor() ?: Color.Unspecified
            )
        }
        DropdownMenu(
            expanded = tagDropDownExpanded,
            onDismissRequest = { tagDropDownExpanded = false },
            modifier = Modifier.background(MaterialTheme.colorScheme.background.copy(alpha = 0.8f))
        ) {
            DropDownTitle(text = stringResource(id = R.string.select_tag))
            DropDownItem(
                item = Tag(name = stringResource(id = R.string.no_tag)),
                onItemSelected = { onTagSelected(null) },
                onDropDownExpanded = { tagDropDownExpanded = false },
                text = stringResource(id = R.string.no_tag),
                color = Color.Gray
            )
            tags.forEach { tag ->
                DropDownItem(
                    item = tag,
                    onItemSelected = { onTagSelected(tag) },
                    onDropDownExpanded = { tagDropDownExpanded = false },
                    text = tag.getDisplayName(),
                    color = tag.uiColor()
                )
            }
        }
    }
}

@Composable
fun DropDownTitle(
    text: String
) {
    DropdownMenuItem(
        text = {
            Text(
                text = text,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onBackground,
                fontWeight = FontWeight.Bold
            )
        },
        onClick = {},
        enabled = false
    )
}

@Composable
fun <T> DropDownItem(
    item: T,
    onItemSelected: (item: T) -> Unit,
    onDropDownExpanded: (Boolean) -> Unit,
    text: String,
    color: Color
) {
    DropdownMenuItem(
        onClick = {
            onItemSelected(item)
            onDropDownExpanded(false)
        },
        text = {
            Text(
                text = text,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onBackground
            )
        },
        leadingIcon = {
            Canvas(modifier = Modifier
                .size(8.dp)
            ) {
                drawCircle(color = color)
            }
        }
    )

}

@ThemePreviews
@Composable
fun FAInputSelectorPreview() {
    FocusedAppTheme {
        FocusedAppBackground {
            FAInputSelector(
                backgroundColor = Color.Yellow,
                onClick = { /*TODO*/ },
                text = "Machine Learning",
                icon = DrawableResourceIcon(FAIcons.project_form_icon)
            )
        }
    }
}