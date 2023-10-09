package com.zn.apps.feature.tags

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import com.zn.apps.ui_design.component.ThemePreviews
import com.zn.apps.ui_design.icon.FAIcons
import com.zn.apps.ui_design.theme.FocusedAppTheme

@Composable
fun SearchTagField(
    onSearchNameChanged: (String) -> Unit,
    modifier: Modifier = Modifier,
    tagName: String = "",
) {
    TextField(
        value = tagName,
        onValueChange = onSearchNameChanged,
        modifier = modifier
            .border(
                border = BorderStroke(width = 1.dp, color = MaterialTheme.colorScheme.primary),
                shape = MaterialTheme.shapes.medium
            ),
        singleLine = true,
        textStyle = TextStyle(
            color = MaterialTheme.colorScheme.onSurface,
            fontStyle = MaterialTheme.typography.bodyLarge.fontStyle,
            fontSize = MaterialTheme.typography.bodyLarge.fontSize,
            fontFamily = MaterialTheme.typography.bodyLarge.fontFamily
        ),
        colors = TextFieldDefaults.colors(
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            cursorColor = MaterialTheme.colorScheme.primary,
            focusedContainerColor = Color.Transparent,
            unfocusedContainerColor = Color.Transparent
        ),
        placeholder = {
            Text(
                text = stringResource(id = R.string.search_tag_hint),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.primary.copy(alpha = 0.4f)
            )
        },
        leadingIcon = {
            Icon(
                painter = painterResource(id = FAIcons.search),
                contentDescription = stringResource(id = R.string.search_tag),
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .size(16.dp)
            )
        }
    )
}

@ThemePreviews
@Composable
fun SearchTagFieldPreview() {
    FocusedAppTheme {
        SearchTagField(onSearchNameChanged = {})
    }
}