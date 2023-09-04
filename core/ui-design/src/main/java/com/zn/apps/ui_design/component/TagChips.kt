package com.zn.apps.ui_design.component

import androidx.compose.animation.animateColor
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.selection.toggleable
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.zn.apps.model.data.tag.Tag
import com.zn.apps.ui_design.R
import com.zn.apps.ui_design.theme.FocusedAppTheme

@Composable
fun TagChips(
    tags: List<Tag>,
    selectedTagId: String,
    onTagSelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {

    val defaultTagName = stringResource(id = R.string.all)

    LazyRow(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier
            .heightIn(min = 16.dp)
            .padding(vertical = 4.dp)
    ) {
        item {
            TagChip(
                tag = Tag(id = "", name = defaultTagName),
                selected = selectedTagId.isEmpty(),
                onTagSelected = onTagSelected
            )
        }

        items(tags, key = { it.id }) { tag ->
            TagChip(
                tag = tag,
                selected = tag.id == selectedTagId,
                onTagSelected = onTagSelected
            )
        }
    }
}

@Composable
fun TagChip(
    tag: Tag,
    selected: Boolean,
    onTagSelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val transition = updateTransition(targetState = selected, label = "tag transition")

    val backgroundColor by transition.animateColor(
        label = "tag backgroundColor",
        transitionSpec = { spring(dampingRatio = Spring.DampingRatioMediumBouncy) }
    ) { isSelected ->
        if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondaryContainer
    }

    val contentColor by transition.animateColor(
        label = "tag contentColor",
        transitionSpec = { tween(durationMillis = 300, easing = LinearOutSlowInEasing) }
    ) { isSelected ->
        if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSecondaryContainer
    }

    val borderColor by transition.animateColor(
        label = "tag borderColor",
        transitionSpec = { tween(durationMillis = 300, easing = FastOutLinearInEasing) }
    ) { isSelected ->
        if (!isSelected) MaterialTheme.colorScheme.primary else Color.Transparent
    }

    Surface(
        color = backgroundColor,
        contentColor = contentColor,
        shape = MaterialTheme.shapes.large,
        modifier = modifier
            .border(
                width = 2.dp,
                color = borderColor,
                shape = MaterialTheme.shapes.large
            )
    ) {

        val interactionSource = remember { MutableInteractionSource() }

        val pressed by interactionSource.collectIsPressedAsState()
        val backgroundPressed =
            if (pressed)
                Modifier.background(MaterialTheme.colorScheme.primary)
            else
                Modifier.background(Color.Transparent)

        Box(modifier = Modifier
            .toggleable(
                value = selected,
                onValueChange = { onTagSelected(tag.id) },
                interactionSource = interactionSource,
                indication = null
            )
            .then(backgroundPressed)
        ) {
            Text(
                text = tag.getDisplayName(),
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(horizontal = 24.dp, vertical = 8.dp)
            )
        }
    }
}

@ThemePreviews
@Composable
fun TagChipSelectedPreview() {
    FocusedAppTheme {
        TagChip(Tag(name = "work"), selected = true, onTagSelected = {})
    }
}

@ThemePreviews
@Composable
fun TagChipNotSelectedPreview() {
    FocusedAppTheme {
        TagChip(Tag(name = "work"), selected = false, onTagSelected = {})
    }
}

@ThemePreviews
@Composable
fun TagChipsPreview() {
    FocusedAppTheme {
        val selectedTag = Tag(name = "work")
        TagChips(
            tags = listOf(
                selectedTag,
                Tag(name = "study"),
                Tag(name = "social"),
                Tag(name = "entertainment")
            ),
            selectedTagId = selectedTag.id,
            onTagSelected = {}
        )
    }
}