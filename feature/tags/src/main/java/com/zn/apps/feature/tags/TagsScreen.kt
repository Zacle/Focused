package com.zn.apps.feature.tags

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.zn.apps.model.data.tag.Tag
import com.zn.apps.ui_common.state.Loading
import com.zn.apps.ui_design.component.FATopAppBar
import com.zn.apps.ui_design.component.ThemePreviews
import com.zn.apps.ui_design.component.dialog.SimpleConfirmationDialog
import com.zn.apps.ui_design.icon.FAIcons
import com.zn.apps.ui_design.icon.Icon
import com.zn.apps.ui_design.theme.FocusedAppTheme
import com.zn.apps.ui_design.util.Colors.TAG_COLORS
import com.zn.apps.ui_design.util.uiColor

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TagsRoute(
    tagsUiModel: TagsUiModel,
    uiStateHolder: TagsUiStateHolder,
    onSearchTag: (String) -> Unit,
    onUpsertTag: (Tag) -> Unit,
    onDeleteTag: (Tag) -> Unit,
    upPress: () -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        topBar = {
            FATopAppBar(
                titleName = stringResource(id = R.string.tags),
                navigationIcon = Icon.ImageVectorIcon(FAIcons.ArrowBack),
                onNavigationIconClicked = { upPress()}
            ) {
                SearchTagField(
                    onSearchNameChanged = onSearchTag,
                    tagName = uiStateHolder.searchQuery
                )
            }
        },
        modifier = modifier.navigationBarsPadding()
    ) { innerPadding ->
        TagsScreen(
            tagsUiModel = tagsUiModel,
            uiStateHolder = uiStateHolder,
            onUpsertTag = onUpsertTag,
            onDeleteTag = onDeleteTag,
            modifier = Modifier.padding(innerPadding)
        )
    }
}

@Composable
fun TagsScreen(
    tagsUiModel: TagsUiModel,
    uiStateHolder: TagsUiStateHolder,
    onUpsertTag: (Tag) -> Unit,
    onDeleteTag: (Tag) -> Unit,
    modifier: Modifier = Modifier
) {
    LoadingContent(
        isLoading = uiStateHolder.loading,
        loadingContent = { Loading() }
    ) {
        TagsList(
            tagsUiModel = tagsUiModel,
            uiStateHolder = uiStateHolder,
            onUpsertTag = onUpsertTag,
            onDeleteTag = onDeleteTag,
            modifier = modifier
        )
    }
}

@Composable
fun LoadingContent(
    isLoading: Boolean,
    loadingContent: @Composable () -> Unit,
    content: @Composable () -> Unit
) {
    if (isLoading) {
        loadingContent()
    } else {
        content()
    }
}

@Composable
fun TagsList(
    tagsUiModel: TagsUiModel,
    uiStateHolder: TagsUiStateHolder,
    onUpsertTag: (Tag) -> Unit,
    onDeleteTag: (Tag) -> Unit,
    modifier: Modifier
) {
    LazyColumn(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        items(tagsUiModel.tags, key = { it.id }) { tag ->
            TagContent(
                tag = tag,
                onUpsertTag = onUpsertTag,
                onDeleteTag = onDeleteTag
            )
        }
        item {
            CreateTag(
                isExactNameFound = uiStateHolder.exactSearchMatchFound,
                tagName = uiStateHolder.searchQuery,
                onCreateTag = onUpsertTag
            )
        }
    }
}

@Composable
fun TagContent(
    tag: Tag,
    onUpsertTag: (Tag) -> Unit,
    onDeleteTag: (Tag) -> Unit,
    modifier: Modifier = Modifier
) {
    Surface {
        Column(
            modifier = modifier
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier
                        .weight(1f)
                ) {
                    Canvas(modifier = Modifier
                        .size(8.dp)
                    ) {
                        drawCircle(color = tag.uiColor())
                    }
                    Text(
                        text = tag.getDisplayName(),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onBackground,
                        fontWeight = FontWeight.Bold
                    )
                }
                TagOptions(
                    tag = tag,
                    onUpsertTag = onUpsertTag,
                    onDeleteTag = onDeleteTag
                )
            }
            Divider()
        }
    }
}

@Composable
fun TagOptions(
    tag: Tag,
    onUpsertTag: (Tag) -> Unit,
    onDeleteTag: (Tag) -> Unit,
    modifier: Modifier = Modifier
) {
    var showDropDownExpanded by remember {
        mutableStateOf(false)
    }
    var showTagDialog by remember {
        mutableStateOf(false)
    }
    var showDeleteTagDialog by remember {
        mutableStateOf(false)
    }
    Box(modifier) {
        IconButton(
            onClick = { showDropDownExpanded = true }
        ) {
            Icon(
                painter = painterResource(id = FAIcons.option_more),
                contentDescription = stringResource(id = R.string.tag_option),
                tint = MaterialTheme.colorScheme.secondary,
                modifier = Modifier.size(24.dp)
            )
        }
        DropdownMenu(
            expanded = showDropDownExpanded,
            onDismissRequest = { showDropDownExpanded = false },
            modifier = Modifier.background(MaterialTheme.colorScheme.background.copy(alpha = 0.8f))
        ) {
            DropdownMenuItem(
                text = {
                    Text(
                        text = stringResource(id = R.string.update_tag),
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                },
                onClick = {
                    showTagDialog = true
                    showDropDownExpanded = false
                }
            )
            DropdownMenuItem(
                text = {
                    Text(
                        text = stringResource(id = R.string.delete_tag),
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                },
                onClick = {
                    showDeleteTagDialog = true
                    showDropDownExpanded = false
                }
            )
        }
    }
    if (showTagDialog) {
        Dialog(onDismissRequest = { showTagDialog = false }) {
            EditTagDialog(
                selectedColorIndex = TAG_COLORS.indexOf(tag.uiColor()),
                onDismissRequest = { showTagDialog = false },
                onDone = { onUpsertTag(it) },
                tag = tag
            )
        }
    }
    if (showDeleteTagDialog) {
        SimpleConfirmationDialog(
            title = R.string.delete_tag,
            text = R.string.delete_tag_text,
            confirmationAction = {
                onDeleteTag(tag)
                showDeleteTagDialog = false
            },
            cancelAction = { showDeleteTagDialog = false }
        )
    }
}

@Composable
fun CreateTag(
    isExactNameFound: Boolean,
    tagName: String,
    onCreateTag: (Tag) -> Unit,
    modifier: Modifier = Modifier
) {
    if (isExactNameFound) return

    var showTagDialog by remember {
        mutableStateOf(false)
    }

    Surface(
        modifier = modifier
    ) {
        Column(
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() }
                ) {
                    showTagDialog = true
                }
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Icon(
                    imageVector = FAIcons.add,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )

                Text(
                    text = stringResource(id = R.string.crate_tag) + " $tagName",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }

    if (showTagDialog) {
        Dialog(onDismissRequest = { showTagDialog = false }) {
            EditTagDialog(
                selectedColorIndex = 0,
                onDismissRequest = { showTagDialog = false },
                onDone = onCreateTag,
                name = tagName
            )
        }
    }
}

@ThemePreviews
@Composable
fun TagContentPreview() {
    FocusedAppTheme {
        TagContent(
            tag = Tag(name = "Sport", color = Color.Yellow.toArgb()),
            onUpsertTag = {},
            onDeleteTag = {}
        )
    }
}

@ThemePreviews
@Composable
fun CreateTagPreview() {
    FocusedAppTheme {
        CreateTag(
            isExactNameFound = false,
            tagName = "Activities",
            onCreateTag = {}
        )
    }
}