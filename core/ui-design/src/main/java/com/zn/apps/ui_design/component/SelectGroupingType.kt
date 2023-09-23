package com.zn.apps.ui_design.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.selection.selectable
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import com.zn.apps.filter.Grouping
import com.zn.apps.filter.Grouping.DeadlineTimeGrouping
import com.zn.apps.filter.Grouping.PriorityGrouping
import com.zn.apps.filter.Grouping.TagGrouping
import com.zn.apps.ui_design.R
import com.zn.apps.ui_design.theme.FocusedAppTheme

@Composable
fun SelectGroupingType(
    currentGrouping: Grouping,
    onGroupingSelected: (Grouping) -> Unit,
    onCancel: () -> Unit,
    modifier: Modifier = Modifier,
    groupList: List<Grouping> =
        listOf(TagGrouping, DeadlineTimeGrouping, PriorityGrouping),
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .padding(bottom = 4.dp),
        shape = MaterialTheme.shapes.medium,
        color = MaterialTheme.colorScheme.surface,
        contentColor = MaterialTheme.colorScheme.onSurface
    ) {
        Column(
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = stringResource(id = R.string.group_tasks),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier
                    .padding(16.dp)
                    .align(Alignment.CenterHorizontally)
            )
            Divider(modifier = Modifier.padding(bottom = 8.dp))
            groupList.forEach { grouping ->
                val selected = currentGrouping == grouping
                SimpleRadioButton(
                    selected = selected,
                    grouping = grouping,
                    onOptionSelected = { onGroupingSelected(grouping) }
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            TextButton(
                modifier = Modifier
                    .padding(bottom = 8.dp, end = 16.dp)
                    .align(Alignment.End),
                onClick = onCancel
            ) {
                Text(
                    text = stringResource(id = R.string.cancel),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}

@Composable
fun SimpleRadioButton(
    selected: Boolean,
    grouping: Grouping,
    onOptionSelected: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .selectable(
                selected = selected,
                onClick = onOptionSelected,
                role = Role.RadioButton
            ),
        contentAlignment = Alignment.CenterStart
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            RadioButton(selected = selected, onClick = null)
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = stringResource(id = Grouping.getGroupingResId(grouping)),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

@ThemePreviews
@Composable
fun SimpleRadioButtonPreview() {
    FocusedAppTheme {
        SimpleRadioButton(
            selected = true,
            grouping = TagGrouping,
            onOptionSelected = {}
        )
    }
}

@ThemePreviews
@Composable
fun SelectGroupingTypePreview() {
    FocusedAppTheme {
        SelectGroupingType(
            currentGrouping = DeadlineTimeGrouping,
            onGroupingSelected = {},
            onCancel = {}
        )
    }
}