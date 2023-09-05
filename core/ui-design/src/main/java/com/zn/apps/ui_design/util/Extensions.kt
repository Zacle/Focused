package com.zn.apps.ui_design.util

import androidx.compose.ui.graphics.Color
import com.zn.apps.model.data.tag.Tag

fun Tag.uiColor(): Color =
    color?.let { Color(it) } ?: Color.Unspecified