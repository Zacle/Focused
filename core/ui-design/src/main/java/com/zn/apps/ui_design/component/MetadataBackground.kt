package com.zn.apps.ui_design.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import com.zn.apps.ui_design.theme.FocusedAppTheme

@Composable
fun IconMetadataBackground(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {

    Surface(
        color = Color.Transparent,
        modifier = modifier
            .clip(MaterialTheme.shapes.large),
        contentColor = Color(0xFF1B0261)
    ) {
        Box(
            Modifier
                .background(
                    color = Color(0xFF8E9DCC)
                )
        ) {
            content()
        }
    }
}

@Composable
fun PercentageMetadataBackground(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Surface(
        color = Color.Transparent,
        modifier = modifier
            .clip(MaterialTheme.shapes.large),
        contentColor = Color(0xFF1B0261)
    ) {
        Box(
            Modifier
                .background(
                    color = Color(0xFF8E9DCC)
                )
        ) {
            content()
        }
    }
}

@ThemePreviews
@Composable
fun IconMetadataBackgroundPreview() {
    FocusedAppTheme {
        IconMetadataBackground {
            Text(text = "Hello World!")
        }
    }
}

@ThemePreviews
@Composable
fun PercentageMetadataBackgroundPreview() {
    FocusedAppTheme {
        PercentageMetadataBackground {
            Text(
                text = "Hello World!"
            )
        }
    }
}