package com.zn.apps.feature.settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.zn.apps.ui_design.component.FATopAppBar
import com.zn.apps.ui_design.component.FocusedAppBackground
import com.zn.apps.ui_design.component.ThemePreviews
import com.zn.apps.ui_design.icon.FAIcons
import com.zn.apps.ui_design.icon.Icon
import com.zn.apps.ui_design.theme.FocusedAppTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsRoute(
    onUpPressed: () -> Unit,
    onPomodoroSettingsPressed: () -> Unit,
    onCustomizeSettingsPressed: () -> Unit,
    onSoundNotificationPressed: () -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        topBar = {
            FATopAppBar(
                titleName = stringResource(id = R.string.settings),
                navigationIcon = Icon.ImageVectorIcon(FAIcons.ArrowBack),
                onNavigationIconClicked = onUpPressed
            ) {}
        },
        modifier = modifier
    ) { innerPadding ->
        SettingsCard(
            onPomodoroSettingsPressed = onPomodoroSettingsPressed,
            onCustomizeSettingsPressed = onCustomizeSettingsPressed,
            onSoundNotificationPressed = onSoundNotificationPressed,
            modifier = modifier
                .padding(innerPadding)
                .padding(
                    top = 32.dp,
                    start = 16.dp,
                    end = 16.dp
                )
        )
    }
}

@Composable
fun SettingsCard(
    onPomodoroSettingsPressed: () -> Unit,
    onCustomizeSettingsPressed: () -> Unit,
    onSoundNotificationPressed: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        tonalElevation = 16.dp,
        shape = MaterialTheme.shapes.medium,
        modifier = modifier
    ) {
        Spacer(modifier = Modifier.height(12.dp))
        Column {
            SettingsRow(
                title = stringResource(id = R.string.pomodoro),
                subtitle = stringResource(id = R.string.pomodoro_settings_description),
                leadingIcon = Icon.DrawableResourceIcon(FAIcons.TimerDestination),
                trailingIcon = Icon.ImageVectorIcon(FAIcons.KeyboardArrowRight),
                onClick = onPomodoroSettingsPressed
            )
            SettingsRow(
                title = stringResource(id = R.string.customize_settings),
                subtitle = stringResource(id = R.string.customize_settings_description),
                leadingIcon = Icon.DrawableResourceIcon(FAIcons.customize_settings),
                trailingIcon = Icon.ImageVectorIcon(FAIcons.KeyboardArrowRight),
                onClick = onCustomizeSettingsPressed
            )
            SettingsRow(
                title = stringResource(id = R.string.sound_notification),
                subtitle = stringResource(id = R.string.reminders),
                leadingIcon = Icon.DrawableResourceIcon(FAIcons.notification),
                trailingIcon = Icon.ImageVectorIcon(FAIcons.KeyboardArrowRight),
                onClick = onSoundNotificationPressed
            )
        }
        Spacer(modifier = Modifier.height(12.dp))
    }
}

@Composable
fun SettingsRow(
    title: String,
    subtitle: String,
    leadingIcon: Icon,
    trailingIcon: Icon,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .clickable { onClick() }
            .padding(horizontal = 8.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        when(leadingIcon) {
            is Icon.DrawableResourceIcon -> {
                Icon(
                    painter = painterResource(id = leadingIcon.id),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        .weight(1f)
                        .size(24.dp)
                )
            }
            is Icon.ImageVectorIcon -> {
                Icon(
                    imageVector = leadingIcon.imageVector,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        .weight(1f)
                        .size(24.dp)
                )
            }
        }
        Spacer(modifier = Modifier.width(12.dp))
        Column(
            modifier = Modifier
                .weight(10f),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Medium
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = subtitle,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.tertiary.copy(alpha = 0.6f)
            )
        }
        when(trailingIcon) {
            is Icon.DrawableResourceIcon -> {
                Icon(
                    painter = painterResource(id = trailingIcon.id),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        .weight(1f)
                        .size(24.dp)
                )
            }
            is Icon.ImageVectorIcon -> {
                Icon(
                    imageVector = trailingIcon.imageVector,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        .weight(1f)
                        .size(24.dp)
                )
            }
        }
    }
}

@ThemePreviews
@Composable
fun SettingsCardPreview() {
    FocusedAppTheme {
        SettingsCard(
            onPomodoroSettingsPressed = {},
            onCustomizeSettingsPressed = {},
            onSoundNotificationPressed = {}
        )
    }
}

@ThemePreviews
@Composable
fun SettingsRowPreview() {
    FocusedAppTheme {
        FocusedAppBackground {
            SettingsRow(
                title = "Sound and Notification",
                subtitle = "Sound, Alarm, Reminders",
                leadingIcon = Icon.DrawableResourceIcon(FAIcons.sound_settings),
                trailingIcon = Icon.ImageVectorIcon(FAIcons.KeyboardArrowRight),
                onClick = {}
            )
        }
    }
}