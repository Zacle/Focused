package com.zn.apps.ui_design.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.zn.apps.ui_design.icon.FAIcons
import com.zn.apps.ui_design.icon.Icon
import com.zn.apps.ui_design.theme.FocusedAppTheme

val CARD_COLOR = Color(0xFF9C4238)

@Composable
fun IconMetadata(
    text: String,
    icon: Icon,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    IconMetadataBackground(
        modifier = modifier
    ) {
        Column(
            modifier = Modifier
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = text,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Medium,
                    textAlign = TextAlign.Left,
                    modifier = Modifier
                        .weight(1f)
                )
                Box(
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(CARD_COLOR.copy(alpha = 0.2f))
                        .padding(8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    when (icon) {
                        is Icon.DrawableResourceIcon -> {
                            Icon(
                                painter = painterResource(id = icon.id),
                                contentDescription = null,
                                tint = CARD_COLOR,
                                modifier = Modifier
                                    .size(24.dp)
                                    .padding(4.dp)
                            )
                        }
                        is Icon.ImageVectorIcon -> {
                            Icon(
                                imageVector = icon.imageVector,
                                contentDescription = null,
                                tint = CARD_COLOR,
                                modifier = Modifier
                                    .size(24.dp)
                                    .padding(4.dp)
                            )
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            content()
        }
    }
}

@Composable
fun CompletionTime(
    hour: Int,
    minute: Int,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = modifier
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        Row(
            verticalAlignment = Alignment.Bottom,
            horizontalArrangement = Arrangement.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "HH",
                    style = MaterialTheme.typography.bodyMedium
                )
                Spacer(modifier = Modifier.size(2.dp))
                Text(
                    text = String.format("%02d", hour),
                    style = MaterialTheme.typography.bodyMedium

                )
            }
            Spacer(modifier = Modifier.size(2.dp))
            Text(
                text = ":",
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodyMedium
            )
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "MM",
                    style = MaterialTheme.typography.bodyMedium
                )
                Spacer(modifier = Modifier.size(2.dp))
                Text(
                    text = String.format("%02d", minute),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}

@Composable
fun PercentageMetadata(
    text: String,
    percentage: Int,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    PercentageMetadataBackground(
        modifier = modifier
    ) {
        Column(
            modifier = Modifier
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = text,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Medium,
                    textAlign = TextAlign.Left,
                    modifier = Modifier
                        .weight(1f)
                )
                CompletionPercentage(percentage = percentage)
            }
            Spacer(modifier = Modifier.height(8.dp))
            content()
        }
    }
}

@Composable
fun CompletionPercentage(
    percentage: Int
) {
    Box(
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(
            progress = 1f,
            color = CARD_COLOR.copy(alpha = 0.2f),
            strokeWidth = 1.dp
        )
        CircularProgressIndicator(
            progress = percentage.toFloat()/100,
            color = CARD_COLOR,
            strokeWidth = 2.dp
        )
        Text(text = "$percentage%")
    }
}

@ThemePreviews
@Composable
fun IconMetadataPreview() {
    FocusedAppTheme {
        IconMetadata(
            text = "Estimated time",
            icon = Icon.DrawableResourceIcon(FAIcons.pomodoro)
        ) {
            CompletionTime(hour = 6, minute = 30)
        }
    }
}

@ThemePreviews
@Composable
fun PercentageMetadataPreview() {
    FocusedAppTheme {
        PercentageMetadata(
            text = "Elapsed Time",
            percentage = 60
        ) {
            CompletionTime(hour = 2, minute = 30)
        }
    }
}

@ThemePreviews
@Composable
fun TaskPercentageMetadataPreview() {
    FocusedAppTheme {
        PercentageMetadata(
            text = "Tasks to be completed",
            percentage = 60
        ) {
            Text(text = "4")
        }
    }
}