package com.zn.apps.feature.tasks.completed

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.zn.apps.ui_design.icon.FAIcons

data class TimeLineCircleParameters(
    val radius: Dp,
    val backgroundColor: Color,
    val circleIcon: Int,
    val circleColor: Color,
    val circleSize: Dp
)

object TimeLineCircleParametersDefault {
    private val defaultCircleRadius = 16.dp
    private val defaultCircleIcon = FAIcons.timeline_icon
    private val defaultCircleSize = 8.dp


    fun circleParameters(
        radius: Dp = defaultCircleRadius,
        backgroundColor: Color = Color.Cyan,
        circleIcon: Int = defaultCircleIcon,
        circleColor: Color = Color.White,
        circleSize: Dp = defaultCircleSize
    ) = TimeLineCircleParameters(radius, backgroundColor, circleIcon, circleColor, circleSize)
}

data class LineParameters(
    val lineWidth: Dp,
    val lineColor: Color
)

object LineParametersDefault {
    private val defaultLineWidth = 2.dp

    fun lineParameters(
        lineWidth: Dp = defaultLineWidth,
        lineColor: Color = Color.Cyan
    ) = LineParameters(lineWidth, lineColor)
}

data class TimeLinePadding(
    val outerPadding: PaddingValues,
    val contentStartPadding: Dp
)

object TimeLinePaddingDefault {
    private val defaultOuterPadding = PaddingValues(16.dp)
    private val defaultContentStartPadding = 8.dp

    fun paddingParameters(
        outerPadding: PaddingValues = defaultOuterPadding,
        contentStartPadding: Dp = defaultContentStartPadding
    ) = TimeLinePadding(outerPadding, contentStartPadding)
}