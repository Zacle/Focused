package com.zn.apps.feature.tasks.completed

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

data class CircleParameters(
    val radius: Dp,
    val backgroundColor: Color,
)

data class LineParameters(
    val strokeWidth: Dp,
    val brush: Brush
)

object CircleParametersDefaults {

    private val defaultCircleRadius = 8.dp

    fun circleParameters(
        radius: Dp = defaultCircleRadius,
        backgroundColor: Color = Color.Cyan,
    ) = CircleParameters(radius, backgroundColor)
}

object LineParametersDefaults {

    private val defaultStrokeWidth = 3.dp

    fun linearGradient(
        strokeWidth: Dp = defaultStrokeWidth,
        startColor: Color,
        endColor: Color,
        startY: Float = 0.0f,
        endY: Float = Float.POSITIVE_INFINITY
    ): LineParameters {
        val brush = Brush.verticalGradient(
            colors = listOf(startColor, endColor),
            startY = startY,
            endY = endY
        )
        return LineParameters(strokeWidth, brush)
    }
}