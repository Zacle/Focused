package com.zn.apps.feature.tags

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.zn.apps.model.data.tag.Tag
import com.zn.apps.ui_design.component.ColorSelector
import com.zn.apps.ui_design.component.ThemePreviews
import com.zn.apps.ui_design.theme.FocusedAppTheme
import com.zn.apps.ui_design.util.Colors.TAG_COLORS

@Composable
fun EditTagDialog(
    selectedColorIndex: Int,
    onDismissRequest: () -> Unit,
    onDone: (Tag) -> Unit,
    modifier: Modifier = Modifier,
    tag: Tag? = null,
    name: String = "",
    maxLength: Int = 20
) {
    var tagName by remember {
        mutableStateOf(tag?.name ?: name)
    }
    var isNameError by remember {
        mutableStateOf(false)
    }
    var colorIndex by remember {
        mutableIntStateOf(
            if (selectedColorIndex != -1) selectedColorIndex else 0
        )
    }

    Surface(
        modifier = modifier
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Column(
            verticalArrangement = Arrangement.Center
        ) {
            TextField(
                value = tagName,
                onValueChange = {
                    if (it.length <= maxLength)
                        tagName = it
                },
                minLines = 2,
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
                        text = stringResource(id = R.string.enter_tag_name),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.primary.copy(alpha = 0.4f)
                    )
                },
                supportingText = {
                    Text(
                        text = "${tagName.length}/$maxLength",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.tertiary.copy(alpha = 0.4f),
                        textAlign = TextAlign.End
                    )
                }
            )
            if (isNameError) {
                Text(
                    text = stringResource(id = R.string.tag_name_error),
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.labelSmall,
                    modifier = Modifier.padding(start = 4.dp)
                )
            }
            Spacer(modifier = Modifier.size(16.dp))
            ColorSelector(
                selectedColorIndex = colorIndex,
                onColorSelected = {
                    colorIndex = it
                },
                colors = TAG_COLORS
            )
            Spacer(modifier = Modifier.size(24.dp))
            Row(
                modifier = Modifier.padding(end = 8.dp, bottom = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.End
            ) {
                Row {
                    Spacer(modifier = Modifier.weight(1.0f))
                    Row {
                        TextButton(
                            onClick = {
                                onDismissRequest()
                            }
                        ) {
                            Text(
                                text = stringResource(id = R.string.cancel),
                                style = MaterialTheme.typography.labelLarge,
                                color = MaterialTheme.colorScheme.error
                            )
                        }
                        Button(
                            onClick = {
                                if (tagName.length < 3) {
                                    isNameError = true
                                } else {
                                    val tagToSave = tag?.copy(name = tagName, color = TAG_COLORS[colorIndex].toArgb())
                                        ?: Tag(name = tagName, color = TAG_COLORS[colorIndex].toArgb())
                                    onDone(tagToSave)
                                    onDismissRequest()
                                }
                            }
                        ) {
                            Text(
                                text = stringResource(id = R.string.done),
                                style = MaterialTheme.typography.labelLarge,
                                color = MaterialTheme.colorScheme.onPrimary
                            )
                        }
                    }
                }
            }
        }
    }
}

@ThemePreviews
@Composable
fun EditTagPreview() {
    FocusedAppTheme {
        EditTagDialog(
            selectedColorIndex = -1,
            onDismissRequest = {},
            onDone = {}
        )
    }
}