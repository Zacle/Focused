package com.zn.apps.ui_design.icon

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.zn.apps.ui_design.component.FocusedAppBackground
import com.zn.apps.ui_design.component.ThemePreviews
import com.zn.apps.ui_design.icon.Icon.DrawableResourceIcon
import com.zn.apps.ui_design.theme.FocusedAppTheme

@Composable
fun FormIcon(
    color: Color,
    icon: DrawableResourceIcon,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(percent = 40))
            .background(color.copy(0.5f))
            .padding(horizontal = 10.dp, vertical = 2.dp),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            painter = painterResource(id = icon.id),
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier
                .padding(4.dp)
                .size(20.dp)
        )
    }
}

@ThemePreviews
@Composable
fun FormIconPreview() {
    FocusedAppTheme {
        FocusedAppBackground {
            FormIcon(
                color = Color.Green,
                icon = DrawableResourceIcon(FAIcons.project_form_icon)
            )
        }
    }
}