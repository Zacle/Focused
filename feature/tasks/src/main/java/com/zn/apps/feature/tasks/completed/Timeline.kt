package com.zn.apps.feature.tasks.completed

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.zn.apps.ui_design.component.ThemePreviews
import com.zn.apps.ui_design.theme.FocusedAppTheme

@Composable
fun TimelineHeader(
    title: String,
    subtitle: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .padding(bottom = 16.dp, top = 8.dp)
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = subtitle,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
        )
    }
}

@Composable
fun Timeline(
    groupIndex: Int,
    elementIndex: Int,
    timeLineCircleParameters: TimeLineCircleParameters,
    lineParameters: LineParameters,
    timeLinePadding: TimeLinePadding,
    isHeader: Boolean,
    header: @Composable () -> Unit,
    content: @Composable () -> Unit,
) {
    ConstraintLayout {
        val (circle, circleInnerLine, topLine, bottomLine, timelineContent) = createRefs()

        TimelineIcon(
            timeLineCircleParameters = timeLineCircleParameters,
            isHeader = isHeader,
            modifier = Modifier
                .constrainAs(circle) {
                    start.linkTo(parent.start)
                    top.linkTo(timelineContent.top)
                    bottom.linkTo(timelineContent.bottom)
                }
        )
        if (!isHeader) {
            Divider(
                modifier = Modifier
                    .constrainAs(circleInnerLine) {
                        top.linkTo(circle.top)
                        bottom.linkTo(circle.bottom)
                        start.linkTo(circle.start)
                        end.linkTo(circle.end)
                        width = Dimension.value(lineParameters.lineWidth)
                        height = Dimension.fillToConstraints
                    },
                color = lineParameters.lineColor
            )
        }
        Surface(
            modifier = Modifier
                .constrainAs(timelineContent) {
                    start.linkTo(circle.end, timeLinePadding.contentStartPadding)
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                }
        ) {
            if (isHeader)
                header()
            else
                content()
        }
        if (!(groupIndex == 0 && elementIndex == HEADER_INDEX)) {
            Divider(
                color = lineParameters.lineColor,
                modifier = Modifier
                    .constrainAs(topLine) {
                        top.linkTo(parent.top)
                        bottom.linkTo(circle.top)
                        start.linkTo(circle.start)
                        end.linkTo(circle.end)
                        width = Dimension.value(lineParameters.lineWidth)
                        height = Dimension.fillToConstraints
                    }
            )
        }
        Divider(
            color = lineParameters.lineColor,
            modifier = Modifier
                .constrainAs(bottomLine) {
                    top.linkTo(circle.bottom)
                    bottom.linkTo(parent.bottom)
                    start.linkTo(circle.start)
                    end.linkTo(circle.end)
                    width = Dimension.value(lineParameters.lineWidth)
                    height = Dimension.fillToConstraints
                }
        )
    }
}

@Composable
fun TimelineIcon(
    timeLineCircleParameters: TimeLineCircleParameters,
    isHeader: Boolean,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(CircleShape)
            .size(timeLineCircleParameters.radius)
            .background(if (!isHeader) Color.Transparent else timeLineCircleParameters.backgroundColor),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            painter = painterResource(id = timeLineCircleParameters.circleIcon),
            contentDescription = null,
            tint = if (isHeader) timeLineCircleParameters.circleColor else Color.Transparent,
            modifier = Modifier.size(timeLineCircleParameters.circleSize)
        )
    }
}

@ThemePreviews
@Composable
fun TimelineIconPreview() {
    FocusedAppTheme {
        TimelineIcon(
            timeLineCircleParameters = TimeLineCircleParametersDefault.circleParameters(
                backgroundColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.7f),
                circleColor = MaterialTheme.colorScheme.onPrimary,
            ),
            isHeader = true
        )
    }
}

const val HEADER_INDEX = -1