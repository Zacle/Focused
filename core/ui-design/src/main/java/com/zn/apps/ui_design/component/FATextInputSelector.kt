package com.zn.apps.ui_design.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.zn.apps.model.data.project.Project
import com.zn.apps.ui_design.icon.FAIcons
import com.zn.apps.ui_design.theme.FocusedAppTheme
import com.zn.apps.ui_design.util.uiColor

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FATextInputSelector(
    title: String,
    leadingIcon: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable (onClick: () -> Unit) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
        modifier = modifier
    ) {
        OutlinedTextField(
            value = title,
            onValueChange = {},
            readOnly = true,
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
            },
            colors = ExposedDropdownMenuDefaults.textFieldColors(
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent
            ),
            leadingIcon = leadingIcon,
            shape = MaterialTheme.shapes.medium,
            modifier = Modifier
                .menuAnchor()
                .fillMaxWidth()
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            content {
                expanded = false
            }
        }
    }
}

@ThemePreviews
@Composable
fun FATextInputSelectorPreview() {
    FocusedAppTheme {
        Surface {
            val list = listOf(
                Project(name = "Machine Learning", color = Color.Magenta.toArgb()),
                Project(name = "UX Design", color = Color.Green.toArgb()),
                Project(name = "Gymnastic", color = Color.Yellow.toArgb()),
            )
            FATextInputSelector(
                title = "Select Project",
                leadingIcon = {
                    Icon(
                        painter = painterResource(id = FAIcons.project_form_icon),
                        contentDescription = null,
                        tint = Color.Gray,
                        modifier = Modifier.size(20.dp)
                    )
                }
            ) { onClick ->
                list.forEach {
                    DropDownItem(
                        item = it,
                        onItemSelected = {},
                        onDropDownExpanded = { onClick() },
                        text = it.name,
                        color = it.uiColor()
                    )
                }
            }
        }
    }
}