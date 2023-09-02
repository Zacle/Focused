package com.zn.apps.data_local.model

import android.graphics.Color
import com.zn.apps.data_local.database.tag.TagEntity
import com.zn.apps.model.data.tag.Tag

fun TagEntity.asExternalModel() =
    Tag(id, name, color)

fun Tag.asEntity() =
    TagEntity(id, name, color)

val INITIAL_TAGS = listOf(
    Tag(
        name = "Sport",
        color = Color.YELLOW
    ),
    Tag(
        name = "Study",
        color = Color.GREEN
    ),
    Tag(
        name = "Work",
        color = Color.parseColor("#FFDDA0DD")
    ),
    Tag(
        name = "Social",
        color = Color.parseColor("#FFD2691E")
    )
).map { it.asEntity() }