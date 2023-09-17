package com.zn.apps.ui_design.component

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.zn.apps.ui_design.icon.FAIcons
import com.zn.apps.ui_design.theme.FocusedAppTheme
import com.zn.apps.ui_design.util.Colors.PROJECT_COLORS

@Composable
fun ColorSelector(
    selectedColorIndex: Int,
    onColorSelected: (Int) -> Unit,
    modifier: Modifier = Modifier,
    colors: List<Color> = PROJECT_COLORS
) {
    Column(
        modifier = modifier
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        (0..2).forEach { index ->
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                colors.subList(index * 5, (index * 5) + 5).forEachIndexed { currentIndex, color ->
                    ColorBox(
                        color = color,
                        selected = selectedColorIndex == (currentIndex + (index * 5)),
                        onSelected = { onColorSelected(currentIndex + (index * 5)) }
                    )
                }
            }
        }
    }
}

@Composable
fun ColorBox(
    color: Color,
    selected: Boolean,
    onSelected: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .background(color, shape = CircleShape)
            .size(40.dp)
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ) { onSelected() },
        contentAlignment = Alignment.Center
    ) {
        val checkMarkColor by animateColorAsState(
            if (selected) Color.Black else Color.Black.copy(alpha = 0.0f), label = ""
        )
        Icon(
            imageVector = FAIcons.check_mark,
            contentDescription = null,
            tint = checkMarkColor
        )
    }
}

@ThemePreviews
@Composable
fun ColorSelectorPreview() {
    FocusedAppTheme {
        ColorSelector(selectedColorIndex = 5, onColorSelected = {})
    }
}