package com.zn.apps.feature.projects.single

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.zn.apps.feature.projects.R
import com.zn.apps.model.data.project.Project
import com.zn.apps.model.data.tag.Tag
import com.zn.apps.ui_common.delegate.ProjectsUiStateHolder
import com.zn.apps.ui_design.R.*
import com.zn.apps.ui_design.component.ColorSelector
import com.zn.apps.ui_design.component.DropDownItem
import com.zn.apps.ui_design.component.FATextInputSelector
import com.zn.apps.ui_design.component.FATopAppBar
import com.zn.apps.ui_design.component.dialog.SimpleConfirmationDialog
import com.zn.apps.ui_design.icon.FAIcons
import com.zn.apps.ui_design.icon.Icon
import com.zn.apps.ui_design.util.Colors
import com.zn.apps.ui_design.util.uiColor

@Composable
fun ProjectRoute(
    projectUiModel: ProjectUiModel,
    uiStateHolder: ProjectsUiStateHolder,
    onUpdatePressed: (Project) -> Unit,
    onDeletePressed: () -> Unit,
    onDeleteDismissed: () -> Unit,
    onDeleteConfirmed: (Project) -> Unit,
    onUpPressed: () -> Unit
) {
    if (uiStateHolder.showDeleteProjectDialog) {
        SimpleConfirmationDialog(
            title = R.string.delete_project,
            text =R.string.delete_project_text,
            confirmationAction = { onDeleteConfirmed(projectUiModel.project) },
            cancelAction = onDeleteDismissed
        )
    }

    ProjectScreen(
        project = projectUiModel.project,
        uiStateHolder = uiStateHolder,
        onUpdatePressed = onUpdatePressed,
        onDeletePressed = onDeletePressed,
        onUpPressed = onUpPressed
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProjectScreen(
    project: Project,
    uiStateHolder: ProjectsUiStateHolder,
    onUpdatePressed: (Project) -> Unit,
    onDeletePressed: () -> Unit,
    onUpPressed: () -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        topBar = {
            FATopAppBar(
                titleName = stringResource(id = R.string.edit_project),
                navigationIcon = Icon.ImageVectorIcon(FAIcons.ArrowBack),
                onNavigationIconClicked = onUpPressed,
                actions = {
                    IconButton(onClick = onDeletePressed) {
                        Icon(
                            painter = painterResource(id = FAIcons.delete),
                            contentDescription = stringResource(id = R.string.delete_project),
                            tint = MaterialTheme.colorScheme.error,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
            ) {}
        },
        modifier = modifier
    ) {
        ProjectContent(
            project = project,
            uiStateHolder = uiStateHolder,
            onUpdatePressed = onUpdatePressed,
            modifier = Modifier
                .padding(it)
        )
    }
}

@Composable
fun ProjectContent(
    project: Project,
    uiStateHolder: ProjectsUiStateHolder,
    onUpdatePressed: (Project) -> Unit,
    modifier: Modifier
) {
    val colorIndex = Colors.PROJECT_COLORS.indexOfFirst { it.toArgb() == project.color }
    val (formState, formStateSetter) = remember {
        mutableStateOf(
            ProjectFormState(
                name = project.name,
                selectedTagId = project.tagId,
                selectedColorIndex = if (colorIndex == -1) 0 else colorIndex
            )
        )
    }

    Surface(
        modifier = modifier
            .fillMaxWidth()
    ) {
        val tag = uiStateHolder.tags.firstOrNull { it.id == formState.selectedTagId }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(24.dp),
            modifier = Modifier
                .padding(horizontal = 12.dp)
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
            ) {
                TextField(
                    value = formState.name,
                    onValueChange = {
                        formStateSetter(
                            formState.copy(name = it)
                        )
                    },
                    placeholder = {
                        Text(text = stringResource(id = R.string.enter_project_name))
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
                        .fillMaxWidth(1f)
                        .padding(bottom = 12.dp),
                    textStyle = TextStyle(
                        fontStyle = MaterialTheme.typography.bodyLarge.fontStyle,
                        fontFamily = MaterialTheme.typography.bodyLarge.fontFamily,
                        fontSize = MaterialTheme.typography.bodyLarge.fontSize,
                        fontWeight = FontWeight.Medium,
                        textDecoration = if (project.completed) TextDecoration.LineThrough else null
                    )
                )
                FATextInputSelector(
                    title = tag?.name ?: stringResource(id = R.string.select_tag),
                    leadingIcon = {
                        Icon(
                            painter = painterResource(id = FAIcons.tag),
                            contentDescription = null,
                            tint = tag?.uiColor() ?: Color.Gray,
                            modifier = Modifier.size(20.dp)
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                ) { onClick ->
                    DropDownItem(
                        item = Tag(name = stringResource(id = string.no_tag)),
                        onItemSelected = {
                            formStateSetter(
                                formState.copy(selectedTagId = null)
                            )
                        },
                        onDropDownExpanded = { onClick() },
                        text = stringResource(id = string.no_tag),
                        color = Color.Gray
                    )
                    uiStateHolder.tags.forEach { tag ->
                        DropDownItem(
                            item = tag,
                            onItemSelected = { item ->
                                formStateSetter(
                                    formState.copy(selectedTagId = item.id)
                                )
                            },
                            onDropDownExpanded = { onClick() },
                            text = tag.name,
                            color = tag.uiColor()
                        )
                    }
                }
                ColorSelector(
                    selectedColorIndex = formState.selectedColorIndex,
                    onColorSelected = {
                        formStateSetter(
                            formState.copy(selectedColorIndex = it)
                        )
                    },
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }
            Button(
                onClick = {
                    val projectToUpdate = project.copy(
                        name = formState.name.ifBlank { project.name },
                        color = Colors.PROJECT_COLORS[formState.selectedColorIndex].toArgb(),
                        tagId = formState.selectedTagId
                    )
                    onUpdatePressed(projectToUpdate)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .align(Alignment.CenterHorizontally)
            ) {
                Text(text = stringResource(id = R.string.update_project))
            }
        }
    }
}

data class ProjectFormState(
    var name: String = "",
    var selectedTagId: String? = null,
    var selectedColorIndex: Int = 0
)
