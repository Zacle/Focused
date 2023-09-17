package com.zn.apps.feature.projects.list

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.zn.apps.feature.projects.R
import com.zn.apps.model.data.project.Project
import com.zn.apps.model.data.tag.Tag
import com.zn.apps.ui_design.component.ColorSelector
import com.zn.apps.ui_design.component.DropDownItem
import com.zn.apps.ui_design.component.DropDownTitle
import com.zn.apps.ui_design.component.FAInputSelector
import com.zn.apps.ui_design.component.FATextInput
import com.zn.apps.ui_design.component.ThemePreviews
import com.zn.apps.ui_design.icon.FAIcons
import com.zn.apps.ui_design.icon.Icon
import com.zn.apps.ui_design.theme.FocusedAppTheme
import com.zn.apps.ui_design.util.Colors
import com.zn.apps.ui_design.util.uiColor

@Composable
fun InsertProjectBottomSheetContent(
    upsertProject: (Project) -> Unit,
    tags: List<Tag>,
    shouldShowModalSheet: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    val (formState, formStateSetter) = remember { mutableStateOf(FormState()) }

    Surface(
        color = Color.Transparent,
        modifier = modifier
            .padding(top = 8.dp)
            .navigationBarsPadding()
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            FATextInput(
                name = formState.name,
                placeholder = stringResource(id = R.string.enter_project_name),
                icon = Icon.DrawableResourceIcon(FAIcons.project_form_icon),
                onValueChange = {
                    formStateSetter(
                        formState.copy(name = it)
                    )
                },
                color = Colors.PROJECT_COLORS[formState.selectedColorIndex]
            )
            SelectTag(
                tags = tags,
                onTagIdSelected = {
                    formStateSetter(
                        formState.copy(selectedTagId = it)
                    )
                },
                selectedTagId = formState.selectedTagId
            )
            ColorSelector(
                selectedColorIndex = formState.selectedColorIndex,
                onColorSelected = {
                    formStateSetter(
                        formState.copy(selectedColorIndex = it)
                    )
                }
            )

            /**
             * Save project to the database
             */
            TextButton(
                enabled = formState.name.isNotEmpty(),
                onClick = {
                    val projectToSave = Project(
                        name = formState.name,
                        color = Colors.PROJECT_COLORS[formState.selectedColorIndex].toArgb(),
                        tagId = formState.selectedTagId,
                    )
                    upsertProject(projectToSave)
                    shouldShowModalSheet(false)
                },
                modifier = Modifier
                    .align(Alignment.End)
            ) {
                Text(
                    text = stringResource(id = R.string.done),
                    style = MaterialTheme.typography.labelLarge,
                    color =
                        if (formState.name.isNotEmpty())
                            MaterialTheme.colorScheme.primary
                        else
                            Color.Gray,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
fun SelectTag(
    selectedTagId: String? = null,
    tags: List<Tag>,
    onTagIdSelected: (String?) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Box {
        val tag = tags.firstOrNull { it.id == selectedTagId }
        val color = tag?.uiColor() ?: MaterialTheme.colorScheme.primary

        FAInputSelector(
            backgroundColor = color,
            onClick = { expanded = true },
            text = tag?.name ?: stringResource(id = R.string.select_tag),
            icon = Icon.DrawableResourceIcon(id = FAIcons.tag)
        )
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.background(MaterialTheme.colorScheme.tertiaryContainer)
        ) {
            DropDownTitle(text = stringResource(id = R.string.select_tag))
            DropDownItem(
                item = Tag(name = stringResource(id = R.string.select_tag)),
                onItemSelected = { onTagIdSelected(null) },
                onDropDownExpanded = { expanded = false },
                text = stringResource(id = com.zn.apps.ui_design.R.string.no_tag),
                color = Color.Gray
            )
            tags.forEach { tag ->
                DropDownItem(
                    item = tag,
                    onItemSelected = { onTagIdSelected(it.id) },
                    onDropDownExpanded = { expanded = false },
                    text = tag.name,
                    color = tag.uiColor()
                )
            }
        }
    }
}

@Immutable
data class FormState(
    var name: String = "",
    var selectedTagId: String? = null,
    var selectedColorIndex: Int = 0
)

@ThemePreviews
@Composable
fun InsertProjectBottomSheetContentPreview() {
    FocusedAppTheme {
        InsertProjectBottomSheetContent(
            upsertProject = {},
            tags = emptyList(),
            shouldShowModalSheet = {}
        )
    }
}