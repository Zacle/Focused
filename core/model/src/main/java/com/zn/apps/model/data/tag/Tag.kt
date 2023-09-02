package com.zn.apps.model.data.tag

import java.util.UUID

/**
 * Model class for tags
 */
data class Tag(
    val id: String = UUID.randomUUID().toString(),
    var name: String,
    var color: Int? = null,
    var selected: Boolean? = false
) {
    fun getDisplayName() = name.lowercase().replaceFirstChar { it.uppercase() }
}

fun Tag.copy(
    name: String = this.name,
    color: Int? = this.color
) =
    Tag(
        id = this.id,
        name = name,
        color = color
    )
