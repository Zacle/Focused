package com.zn.apps.feature.projects.list

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.zn.apps.feature.projects.R
import com.zn.apps.model.data.project.ProjectFilterType
import com.zn.apps.ui_design.component.ThemePreviews
import com.zn.apps.ui_design.theme.FocusedAppTheme

@Composable
fun ProjectsAppBarFilter(
    onFilterSelected: (ProjectFilterType) -> Unit,
    selected: ProjectFilterType,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .wrapContentSize()
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.primary,
                shape = MaterialTheme.shapes.medium
            ),
        contentAlignment = Alignment.Center
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = 8.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            FilterType(
                text = stringResource(id = R.string.all),
                selected = selected == ProjectFilterType.ALL,
                onSelected = { onFilterSelected(ProjectFilterType.ALL) },
                modifier = Modifier
                    .weight(1f)
            )
            FilterType(
                text = stringResource(id = R.string.ongoing),
                selected = selected == ProjectFilterType.ONGOING,
                onSelected = { onFilterSelected(ProjectFilterType.ONGOING) },
                modifier = Modifier
                    .weight(1f)
            )
            FilterType(
                text = stringResource(id = R.string.completed),
                selected = selected == ProjectFilterType.COMPLETED,
                onSelected = { onFilterSelected(ProjectFilterType.COMPLETED) },
                modifier = Modifier
                    .weight(1f)
            )
        }
    }
}

@Composable
fun FilterType(
    text: String,
    selected: Boolean,
    onSelected: () -> Unit,
    modifier: Modifier = Modifier
) {
    val backgroundColor by animateColorAsState(
        targetValue = if (selected) MaterialTheme.colorScheme.primary else Color.Transparent,
        label = "filterType backgroundColor"
    )

    val contentColor by animateColorAsState(
        targetValue = if (selected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.primary,
        label = "filterType contentColor"
    )

    Box(
        modifier = modifier
            .background(color = backgroundColor, shape = MaterialTheme.shapes.medium)
            .clickable { onSelected() },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.bodyMedium,
            color = contentColor,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
        )
    }
}

@ThemePreviews
@Composable
fun ProjectsAppBarFilterPreview() {
    FocusedAppTheme {
        ProjectsAppBarFilter(onFilterSelected = {}, selected = ProjectFilterType.ALL)
    }
}