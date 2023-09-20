package com.zn.apps.feature.tasks.single

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.zn.apps.feature.tasks.R
import com.zn.apps.model.data.tag.Tag
import com.zn.apps.model.data.task.TaskPriority
import com.zn.apps.ui_design.component.ChoosePriority
import com.zn.apps.ui_design.component.DropDownItem
import com.zn.apps.ui_design.component.DropDownTitle
import com.zn.apps.ui_design.component.ThemePreviews
import com.zn.apps.ui_design.icon.FAIcons
import com.zn.apps.ui_design.theme.FocusedAppTheme
import com.zn.apps.ui_design.util.uiColor
import com.zn.apps.ui_design.R as uiDesignR

@Composable
fun TaskTextField(
    name: String,
    priority: TaskPriority,
    completed: Boolean,
    tags: List<Tag>,
    onNameChanged: (String) -> Unit,
    onPriorityChanged: (TaskPriority) -> Unit,
    onSelectedTagIdChanged: (String?) -> Unit,
    modifier: Modifier = Modifier,
    selectedTagId: String? = null
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(start = 4.dp)
    ) {
        Row {
            TextField(
                value = name,
                onValueChange = onNameChanged,
                placeholder = {
                    Text(text = stringResource(id = R.string.enter_task_name))
                },
                minLines = 2,
                shape = MaterialTheme.shapes.extraSmall,
                colors = TextFieldDefaults.colors(
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    focusedTextColor = MaterialTheme.colorScheme.onSurface,
                    unfocusedTextColor = MaterialTheme.colorScheme.onSurface
                ),
                modifier = Modifier
                    .weight(1f),
                textStyle = TextStyle(
                    fontStyle = MaterialTheme.typography.bodyLarge.fontStyle,
                    fontFamily = MaterialTheme.typography.bodyLarge.fontFamily,
                    fontSize = MaterialTheme.typography.bodyLarge.fontSize,
                    fontWeight = FontWeight.Medium,
                    textDecoration = if (completed) TextDecoration.LineThrough else null
                )
            )
            ChoosePriority(
                onPrioritySelected = onPriorityChanged,
                selectedPriority = priority,
                modifier = Modifier.padding(top = 4.dp)
            )
        }
        SelectTagChip(
            tags = tags,
            selectedTagId = selectedTagId,
            onTagSelected = onSelectedTagIdChanged,
            modifier = Modifier
                .padding(start = 8.dp)
        )
    }
}

@Composable
fun SelectTagChip(
    tags: List<Tag>,
    selectedTagId: String?,
    onTagSelected: (String?) -> Unit,
    modifier: Modifier = Modifier
) {
    var tagDropDownExpanded by remember {
        mutableStateOf(false)
    }
    Box(
        modifier = modifier
    ) {
        val tag = tags.firstOrNull { it.id == selectedTagId }

        AssistChip(
            onClick = { tagDropDownExpanded = true },
            label = {
                Text(
                    text = tag?.getDisplayName() ?: stringResource(id = uiDesignR.string.no_tag)
                )
            },
            leadingIcon = {
                Icon(
                    painter = painterResource(id = FAIcons.tag),
                    contentDescription = null,
                    tint = tag?.uiColor()?.copy(alpha = 0.7f) ?: Color.Gray,
                    modifier = Modifier.size(20.dp)
                )
            },
            colors = AssistChipDefaults.assistChipColors(
                containerColor = tag?.uiColor()?.copy(alpha = 0.7f) ?: MaterialTheme.colorScheme.surface
            )
        )
        DropdownMenu(
            expanded = tagDropDownExpanded,
            onDismissRequest = { tagDropDownExpanded = false },
            modifier = Modifier.background(MaterialTheme.colorScheme.tertiaryContainer)
        ) {
            DropDownTitle(text = stringResource(id = uiDesignR.string.select_tag))
            DropDownItem(
                item = Tag(name = stringResource(id = uiDesignR.string.no_tag)),
                onItemSelected = { onTagSelected(null) },
                onDropDownExpanded = { tagDropDownExpanded = false },
                text = stringResource(id = uiDesignR.string.no_tag),
                color = Color.Gray
            )
            tags.forEach { tag ->
                DropDownItem(
                    item = tag,
                    onItemSelected = { onTagSelected(tag.id) },
                    onDropDownExpanded = { tagDropDownExpanded = false },
                    text = tag.getDisplayName(),
                    color = tag.uiColor()
                )
            }
        }
    }
}

@ThemePreviews
@Composable
fun TaskTextFieldPreview() {
    FocusedAppTheme {
        Surface {
            TaskTextField(
                name = "Design",
                priority = TaskPriority.HIGH,
                tags = emptyList(),
                onNameChanged = {},
                onPriorityChanged = {},
                onSelectedTagIdChanged = {},
                completed = true
            )
        }
    }
}