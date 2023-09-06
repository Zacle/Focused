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

    /** Only IDs are used for equality. */
    override fun equals(other: Any?): Boolean = this === other || (other is Tag && other.id == id)

    /** Only IDs are used for equality. */
    override fun hashCode(): Int = id.hashCode()

    fun getDisplayName() = name.lowercase().replaceFirstChar { it.uppercase() }
}